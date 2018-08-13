package org.iata.bsplink.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.utils.UserUtils;
import org.iata.bsplink.user.validation.ValidationMessages;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@CommonsLog
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
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

        if (userRepository.findById(user.getId()).isPresent()) {
            errors.rejectValue("id", "", ValidationMessages.USER_ALREADY_EXISTS);
            throw new ApplicationValidationException(errors);
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "", ValidationMessages.USER_ALREADY_EXISTS);
            throw new ApplicationValidationException(errors);
        }

        return userRepository.save(user);
    }

    /**
     * Updates user.
     */
    @Override
    public User updateUser(User userToUpdate, User newUser) {

        log.info("Updating resource with id: " + userToUpdate);

        userToUpdate = UserUtils.mapUserToUpdate(userToUpdate, newUser);
        userToUpdate.setLastModifiedDate(LocalDateTime.now());

        return userRepository.save(userToUpdate);

    }

    /**
     * Deletes user.
     */
    @Override
    public void deleteUser(User user) {

        log.info("Deleting resource with id: " + user);

        userRepository.delete(user);

    }


}
