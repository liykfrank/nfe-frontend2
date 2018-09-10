package org.iata.bsplink.filemanager.service;

import org.iata.bsplink.filemanager.exception.FeignClientException;
import org.iata.bsplink.filemanager.pojo.User;
import org.iata.bsplink.filemanager.restclient.UserClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }


    /**
     * Returns a User.
     */
    public User findUser(String id) {

        ResponseEntity<User> respUser = userClient.findUser(id);

        if (!respUser.getStatusCode().isError()) {
            return respUser.getBody();
        }

        if (respUser.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return null;
        }

        throw new FeignClientException(respUser.getStatusCode());
    }


    /**
     * Checks if user exists.
     */
    public boolean existsUser(String id) {
        return findUser(id) != null;
    }
}
