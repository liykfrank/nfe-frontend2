package org.iata.bsplink.user.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.validation.Errors;

public interface UserService {

    Optional<User> getUser(String userId);
    
    List<User> findByUserType(UserType userType);

    User createUser(User user, Errors errors);

    User updateUser(User userId, User newUser, Errors errors);

    void deleteUser(User user);

}