package org.iata.bsplink.user.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.extern.java.Log;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.service.UserService;
import org.iata.bsplink.user.validation.AgentValidator;
import org.iata.bsplink.user.validation.AirlineValidator;
import org.iata.bsplink.user.validation.UserTemplateValidator;
import org.iata.bsplink.user.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentValidator agentValidator;

    @Autowired
    private AirlineValidator airlineValidator;

    @Autowired
    private UserTemplateValidator userTemplateValidator;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Stream.of(agentValidator, airlineValidator, userTemplateValidator)
                .forEach(validator -> Optional.ofNullable(binder.getTarget())
                        .filter(o -> validator.supports(o.getClass()))
                        .ifPresent(o -> binder.addValidators(validator)));
    }



    /**
     * Exposes a REST endpoint that returns the user matching the provided id.
     *
     * @param userId the user id
     *
     * @return the user matching the provided id
     */
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiImplicitParam(name = "id", value = "The ID of the user", required = true, type = "int")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 400, message = "Invalid user ID"),
            @ApiResponse(code = 404, message = "User not found")})
    public ResponseEntity<User> getUser(@NotBlank @PathVariable("id") String userId) {

        log.info("received request for getting user with id: {}" + userId);

        Optional<User> user = userService.getUser(userId);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        log.info("responding with user: " + user.get());

        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    /**
     * Exposes a REST endpoint that creates a new user.
     *
     * @param request details for creating the user
     *
     * @return the created user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public User createUser(@Valid @RequestBody User request, Errors errors) {

        log.info("received request for creating user: " + request);

        userValidator.validateCreate(request, errors);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        User response = userService.createUser(request, errors);

        log.info("responding with response: " + response);

        return response;
    }

    /**
     * Exposes a REST endpoint that updates the user for the provided id.
     *
     * @param userId the user id
     * @param request details for update the user
     *
     * @return the updated user
     */
    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<User> updateUser(@Valid @RequestBody User request, Errors errors,
            @NotBlank @PathVariable("id") String userId) {

        log.info("received request for updating user with id: " + userId);

        userValidator.validateUpdate(request, errors);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        Optional<User> user = userService.getUser(userId);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User response = userService.updateUser(user.get(), request, errors);

        log.info("responding with response: " + response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Exposes a REST endpoint that deletes the user for the provided id.
     *
     * @param userId the user id
     * @return User
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<User> deleteUser(@NotBlank @PathVariable("id") String userId) {

        log.info("received request for deleting user with id: " + userId);

        Optional<User> user = userService.getUser(userId);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(user.get());

        log.info("user successfully deleted");

        return ResponseEntity.ok().build();
    }

}
