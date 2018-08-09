package org.iata.bsplink.refund.validation;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Composite validator for refunds.
 */
public class RefundCompositeValidator implements Validator {

    private List<Validator> validators;

    public RefundCompositeValidator(List<Validator> validators) {

        this.validators = validators;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        for (Validator validator : validators) {

            if (validator.supports(clazz)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

        for (Validator validator : validators) {

            if (validator.supports(target.getClass())) {

                validator.validate(target, errors);
            }
        }
    }

}
