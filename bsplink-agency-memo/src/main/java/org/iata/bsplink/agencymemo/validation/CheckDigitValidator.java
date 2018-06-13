package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.validation.constraints.CheckDigitConstraint;


public class CheckDigitValidator
        implements ConstraintValidator<CheckDigitConstraint, Integer> {

    @Override
    public boolean isValid(Integer checkDigit,
            ConstraintValidatorContext context) {

        return checkDigit == null || checkDigit == 9 || (checkDigit >= 0 && checkDigit < 7);
    }
}
