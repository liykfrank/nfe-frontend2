package org.iata.bsplink.user.validation;

import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.service.AirlineService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AirlineValidator implements Validator {

    private static final String USER_CODE = "userCode";
    private static final Integer USER_CODE_LENGTH = 3;
    private static final String USER_CODE_ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";
    private static final String USER_CODE_ALPHANUMERIC_MESSAGE = "Field must be alphanumeric";
    private static final String AIRLINE_NOT_FOUND_MESSAGE = "Airline does not exist.";
    private static final String USER_CODE_MIN_LENGTH_MESSAGE = "User code length must be ";

    private AirlineService airlineService;

    public AirlineValidator(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getUserType().equals(UserType.AIRLINE)) {
           
            if (user.getUserCode().length() != USER_CODE_LENGTH) {
                errors.rejectValue(USER_CODE, "", USER_CODE_MIN_LENGTH_MESSAGE + USER_CODE_LENGTH);
            }

            if (!user.getUserCode().matches(USER_CODE_ALPHANUMERIC_REGEX)) {
                errors.rejectValue(USER_CODE, "", USER_CODE_ALPHANUMERIC_MESSAGE);
            }

            if (airlineService.findAirline("AA", user.getUserCode()) == null) {
                errors.rejectValue(USER_CODE, "", AIRLINE_NOT_FOUND_MESSAGE);
            }
        }
    }
}
