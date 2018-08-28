package org.iata.bsplink.user.validation;

import java.util.Optional;
import lombok.extern.java.Log;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserStatus;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.service.KeycloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log
@Component
public class UserValidator {

    private static final String USER_CODE_MIN_LENGTH_MESSAGE = "Length must be grather than ";
    private static final String USER_CODE_ALPHANUMERIC_MESSAGE = "Field must be alphanumeric";
    private static final String USER_CODE_MUST_BE_NULL_MESSAGE = "Field must be null";
    private static final String USER_CODE = "userCode";
    private static final Integer USER_CODE_MIN_LENGTH = 6;
    private static final String USER_CODE_ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";

    private UserRepository userRepository;
    private KeycloakService keycloakService;

    public UserValidator(UserRepository userRepository, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    /**
     * Validates basic user information.
     * 
     * @param user to update.
     * @param errors validations.
     */
    public void validateBasicUserInfo(User user, Errors errors) {

        log.info("Validate user: " + user);

        if (user.getUserType().equals(UserType.THIRDPARTY)
                || user.getUserType().equals(UserType.BSP)) {

            if (user.getUserCode().length() < USER_CODE_MIN_LENGTH) {
                errors.rejectValue(USER_CODE, "",
                        USER_CODE_MIN_LENGTH_MESSAGE + USER_CODE_MIN_LENGTH);
            }

            if (!user.getUserCode().matches(USER_CODE_ALPHANUMERIC_REGEX)) {
                errors.rejectValue(USER_CODE, "", USER_CODE_ALPHANUMERIC_MESSAGE);
            }

        } else if (user.getUserType().equals(UserType.DPC) && user.getUserCode() != null) {
            errors.rejectValue(USER_CODE, "", USER_CODE_MUST_BE_NULL_MESSAGE);
        }
    }

    /**
     * Validates user to be created.
     * 
     * @param user to validate.
     * @param errors of validations.
     */
    public void validateCreate(User user, Errors errors) {

        Optional<User> userFoundByUsername = userRepository.findByUsername(user.getUsername());
        UserRepresentation userFoundedKeycloak = keycloakService.findUser(user);

        validateBasicUserInfo(user, errors);

        if (userFoundByUsername.isPresent()
                && userFoundByUsername.get().getStatus().equals(UserStatus.CREATED)
                && userFoundedKeycloak != null) {
            errors.rejectValue("username", "", ValidationMessages.USER_ALREADY_EXISTS);
            throw new ApplicationValidationException(errors);
        }
    }

    /**
     * Validates user to be updated.
     * 
     * @param user to update.
     * @param errors of validations.
     */
    public void validateUpdate(User user, Errors errors) {

        validateBasicUserInfo(user, errors);
    }
}
