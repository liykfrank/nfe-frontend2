package org.iata.bsplink.user.service;

import java.util.List;

import javax.ws.rs.core.Response;

import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserStatus;
import org.iata.bsplink.user.validation.ValidationMessages;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Data
@Service
@CommonsLog
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    private Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Finds user by username or email.
     * 
     * @param user to find
     * @return UserRepresentation
     */
    public UserRepresentation findUser(User user) {

        log.info("Searching for user in keycloak: " + user.getId());

        UsersResource usersResource = getKeycloak().realm(realm).users();

        List<UserRepresentation> listUserKeycloackByUsername =
                usersResource.search(user.getUsername());

        if (!listUserKeycloackByUsername.isEmpty()) {
            return listUserKeycloackByUsername.get(0);
        }

        log.info("User not found in keycloak");

        return null;
    }

    /**
     * Updates user.
     * 
     * @param user to update
     * @return UserRepresentation
     */
    public UserRepresentation updateUser(User user, Errors errors) {

        log.info("Updating user in keycloak: " + user);

        UsersResource usersResource = getKeycloak().realm(realm).users();
        List<UserRepresentation> listUsers = usersResource.search(user.getUsername());

        if (!listUsers.isEmpty()) {

            UserRepresentation userToUpdate = new UserRepresentation();
            userToUpdate.setFirstName(user.getName());
            userToUpdate.setLastName(user.getLastName());

            usersResource.get(user.getId()).update(userToUpdate);

            log.info("User updated");

            return findUser(user);

        } else {

            log.info("User not found");

            errors.rejectValue("username", "", ValidationMessages.USER_NOT_FOUND);
            throw new ApplicationValidationException(errors);
        }

    }

    /**
     * Creates user in keycloak.
     * 
     * @param user User to create.
     * @return Response
     */
    public Response createUser(User user) {

        log.info("Creating new user in keycloak: " + user);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getUsername());

        if (user.getStatus() == null || user.getStatus().equals(UserStatus.PENDING)) {
            userRepresentation.setEnabled(false);
        } else if (user.getStatus().equals(UserStatus.CREATED)) {
            userRepresentation.setEnabled(true);
        }

        Response response = getKeycloak().realm(realm).users().create(userRepresentation);

        log.info("User created in keycloak");

        return response;
    }

    /**
     * Updates user status.
     * 
     * @param username string
     * @param status true or false
     * @return boolean
     */
    public UserRepresentation changeUserStatus(String username, boolean status, Errors errors) {

        log.info("Updating user status in keycloak with username: " + username);

        UsersResource usersResource = getKeycloak().realm(realm).users();
        List<UserRepresentation> listUserKeycloack = usersResource.search(username);

        if (!listUserKeycloack.isEmpty()) {

            UserRepresentation user = listUserKeycloack.get(0);
            user.setEnabled(true);
            usersResource.get(user.getId()).update(user);

            log.info("User status updated");

            return usersResource.search(username).get(0);

        } else {
            log.info("User not found in keycloak");
            errors.rejectValue("username", "", ValidationMessages.USER_NOT_FOUND);
            throw new ApplicationValidationException(errors);
        }
    }


    /**
     * Deletes user in keycloak.
     * 
     * @param id to delete
     * @return Response
     */
    public Response deleteUser(String id) {

        log.info("Deleting user in keycloak with id: " + id);

        Response reponse = getKeycloak().realm(realm).users().delete(id);

        log.info("User deleted in keycloak");

        return reponse;
    }
}
