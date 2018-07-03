package org.iata.bsplink.agentmgmt.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.flywaydb.core.internal.util.StringUtils;
import org.iata.bsplink.agentmgmt.validation.constraints.ControlDigitConstraint;

public class ControlDigitValidator implements ConstraintValidator<ControlDigitConstraint, String> {

    private static final int CONTROL_DIGIT_DIVISOR = 7;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true;
        }

        if (!(value.length() > 1 && StringUtils.isNumeric(value))) {
            return false;
        }

        return hasExpectedControlDigit(value);
    }

    private boolean hasExpectedControlDigit(String value) {

        int baseNumber = Integer.parseInt(value.substring(0, value.length() - 1));
        int actualControlDigit = Integer.parseInt(value.substring(value.length() - 1));
        int expectedControlDigit = baseNumber % CONTROL_DIGIT_DIVISOR;

        return actualControlDigit == expectedControlDigit;
    }

}
