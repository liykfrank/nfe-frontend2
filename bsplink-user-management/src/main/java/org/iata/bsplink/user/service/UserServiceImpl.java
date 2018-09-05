package org.iata.bsplink.user.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.http.HttpStatus;
import org.iata.bsplink.commons.rest.exception.ApplicationInternalServerError;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserStatus;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.model.repository.UserTemplateRepository;
import org.iata.bsplink.user.utils.UserUtils;
import org.iata.bsplink.user.validation.ValidationMessages;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
@CommonsLog
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserTemplateRepository userTemplateRepository;
    private KeycloakService keycloakService;

    public static final String USER_DELETED = "User deleted.";
    public static final String CREATE_USER_ERROR_MESSAGE = "Could not create user.";
    public static final String CREATE_USER_IN_KEYCLOAK_ERROR_MESSAGE =
            "Could not create user in keycloak.";
    public static final String CHANGE_USER_STATUS_IN_KEYCLOAK_ERROR_MESSAGE =
            "Error changing user status in keycloak.";


    /**
     * Parameters constructor.
     *
     * @param userRepository helps to save user.
     * @param keycloakService saves user in keycloak.
     */
    public UserServiceImpl(UserRepository userRepository,
            UserTemplateRepository userTemplateRepository, KeycloakService keycloakService) {

        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.userTemplateRepository = userTemplateRepository;
    }

    /**
     * Returns user by user id.
     */
    @Override
    public Optional<User> getUser(String userId) {

        log.info("Getting user with id: " + userId);

        return userRepository.findById(userId);
    }

    /**
     * Creates user.
     *
     */
    @Override
    public User createUser(User user, Errors errors) {

        log.info("Creating new user: " + user);

        User newUser = null;
        verifyFoundUserAndStatus(user, errors);
        UserRepresentation newUserKeycloak = createUserInKeycloak(user);

        if (newUserKeycloak != null) {

            user.setId(newUserKeycloak.getId());
            user.setStatus(UserStatus.PENDING);
            newUser = userRepository.save(user);

            UserRepresentation userRepresentation =
                    keycloakService.changeUserStatus(user.getUsername(), true, errors);

            if (userRepresentation != null) {

                newUser.setStatus(UserStatus.CREATED);
                newUser = userRepository.save(newUser);

                log.info("New user created: " + newUser);

            } else {
                throw new ApplicationInternalServerError(
                        CHANGE_USER_STATUS_IN_KEYCLOAK_ERROR_MESSAGE);
            }

        } else {
            throw new ApplicationInternalServerError(CREATE_USER_ERROR_MESSAGE);
        }

        return newUser;
    }


    /**
     * Updates user.
     */
    @Override
    @Transactional
    public User updateUser(User userToUpdate, User newUser, Errors errors) {

        log.info("Updating resource with id: " + userToUpdate);

        Set<String> newTemplatesIds;

        if (newUser.getTemplates() == null) {
            newTemplatesIds = Collections.emptySet();
        } else {
            newTemplatesIds = newUser.getTemplates().stream().map(UserTemplate::getId)
                    .collect(Collectors.toSet());
        }

        List<UserTemplate> oldTemplates;

        if (userToUpdate.getTemplates() == null) {
            oldTemplates = Collections.emptyList();
        } else {
            oldTemplates = userToUpdate.getTemplates().stream()
                    .filter(t -> newTemplatesIds.contains(t.getId())).collect(Collectors.toList());
        }

        userToUpdate = UserUtils.mapUserToUpdate(userToUpdate, newUser);
        userToUpdate.setLastModifiedDate(LocalDateTime.now());
        User userUpdated = userRepository.save(userToUpdate);
        userTemplateRepository.deleteAll(oldTemplates);
        keycloakService.updateUser(userUpdated, errors);

        log.info("User updated");

        return userUpdated;
    }


    /**
     * Deletes user.
     */
    @Override
    public void deleteUser(User user) {

        log.info("Deleting user: " + user.getId());

        userRepository.delete(user);

        if (keycloakService.findUser(user) != null) {

            log.info("Deleting user in keycloak");

            keycloakService.deleteUser(user.getId());

        }

        log.info(USER_DELETED);
    }


    private UserRepresentation createUserInKeycloak(User user) {

        Response response = keycloakService.createUser(user);

        if (response.getStatus() == HttpStatus.SC_CREATED) {
            return keycloakService.findUser(user);
        } else {
            throw new ApplicationInternalServerError(CREATE_USER_IN_KEYCLOAK_ERROR_MESSAGE);
        }
    }

    private void verifyFoundUserAndStatus(User user, Errors errors) {

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        UserRepresentation foundUserKeycloak = keycloakService.findUser(user);

        if (foundUser.isPresent() && foundUser.get().getStatus().equals(UserStatus.CREATED)
                && foundUserKeycloak != null) {

            errors.rejectValue("username", "", ValidationMessages.USER_ALREADY_EXISTS);
            throw new ApplicationValidationException(errors);

        } else if (foundUser.isPresent() && (foundUser.get().getStatus().equals(UserStatus.PENDING)
                || (foundUser.get().getStatus().equals(UserStatus.CREATED)
                        && foundUserKeycloak == null))) {

            log.info("Found user with status: " + foundUser.get().getStatus());

            userRepository.delete(foundUser.get());

            if (foundUserKeycloak != null) {

                keycloakService.deleteUser(foundUserKeycloak.getId());
            }

            log.info(USER_DELETED);

        } else if (!foundUser.isPresent() && foundUserKeycloak != null) {

            log.info("Found user in keycloak");

            keycloakService.deleteUser(foundUserKeycloak.getId());

            log.info(USER_DELETED);
        }
    }
}
