package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegularizationNotNullValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AcdmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AcdmRequest acdm = (AcdmRequest) target;

        if (acdm.getRegularized() == null) {
            errors.rejectValue("regularized", "field.null_value",
                    ValidationMessages.NON_NULL_MESSAGE);
        }

    }
}
