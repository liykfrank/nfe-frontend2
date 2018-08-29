package org.iata.bsplink.user.utils;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.validation.ValidationMessages;
import org.springframework.validation.Errors;

public class AgentCodeUtility {

    private static final String USER_CODE = "userCode";
    public static final String INCORRECT_CHECK_DIGIT_MSG = "Incorrect Check-Digit";
    public static final String INVALID_CHECK_DIGIT_MSG =
            "Incorrect Format. Expected: 7 digit agent code + Check-Digit";

    private AgentCodeUtility() {}

    /**
     * Validates Agent Code.
     */
    public static void validateAgentCode(String agentCode, Errors errors) {

        if (!agentCode.matches("^[0-9]{8}$")) {
            errors.rejectValue(USER_CODE, "", ValidationMessages.INCORRECT_FORMAT);
        }

        if (!agentCode.matches(".*[0-6]$")) {
            errors.rejectValue(USER_CODE, "", INVALID_CHECK_DIGIT_MSG);
        }

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }
        
        int code = Integer.parseInt(agentCode.substring(0, 7));
        int checkDigit = Integer.parseInt(agentCode.substring(7));

        if (code % 7 != checkDigit) {
            errors.rejectValue(USER_CODE, "", INCORRECT_CHECK_DIGIT_MSG);
        }
    }
}
