package org.iata.bsplink.service;

import java.util.Optional;
import org.iata.bsplink.model.entity.User;
import org.springframework.validation.Errors;

public interface UserService {

    Optional<User> getUser(String userId);

    User createUser(User user, Errors errors);

    User updateUser(User userId, User newUser);

    void deleteUser(User user);

}