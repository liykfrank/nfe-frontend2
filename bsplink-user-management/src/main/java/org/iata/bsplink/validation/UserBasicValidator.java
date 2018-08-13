package org.iata.bsplink.validation;

import lombok.extern.java.Log;
import org.iata.bsplink.model.entity.User;
import org.iata.bsplink.model.entity.UserType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log
@Component
public class UserBasicValidator {

    private static final String USER_CODE_MIN_LENGTH_MESSAGE = "Length must be grather than ";
    private static final String USER_CODE_ALPHANUMERIC_MESSAGE = "Field must be alphanumeric";
    private static final String USER_CODE_MUST_BE_NULL_MESSAGE = "Field must be null";
    private static final String USER_CODE = "userCode";
    private static final Integer USER_CODE_MIN_LENGTH = 6;
    private static final String USER_CODE_ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";

    /**
     * Validates user basic fields.
     * 
     * @param user To validate.
     * @param errors Collection of errors.
     */
    public void validate(User user, Errors errors) {

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
}
