package org.iata.bsplink.refund.utils;

import org.iata.bsplink.refund.exception.CustomValidationException;
import org.iata.bsplink.refund.validation.ValidationMessages;

public class AgentCodeUtility {

    public static final String INCORRECT_CHECK_DIGIT_MSG = "Incorrect Check-Digit";
    public static final String INVALID_CHECK_DIGIT_MSG
        = "Incorrect Format. Expected: 7 digit agent code + Check-Digit";

    private AgentCodeUtility() {
    }

    /**
     * Validates Agent Code.
     */
    public static void validateAgentCode(String agentCode) throws CustomValidationException {

        if (!agentCode.matches("^[0-9]{8}$")) {
            throw new CustomValidationException("field.incorrect_format",
                    ValidationMessages.INCORRECT_FORMAT);
        }

        if (!agentCode.matches(".*[0-6]$")) {
            throw new CustomValidationException("field.invalid_check_digit",
                    INVALID_CHECK_DIGIT_MSG);
        }

        int code = Integer.parseInt(agentCode.substring(0, 7));
        int checkDigit = Integer.parseInt(agentCode.substring(7));

        if (code % 7 != checkDigit) {
            throw new CustomValidationException("field.incorrect_check_digit",
                    INCORRECT_CHECK_DIGIT_MSG);
        }
    }
}
