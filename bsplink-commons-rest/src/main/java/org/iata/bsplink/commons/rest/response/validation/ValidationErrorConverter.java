package org.iata.bsplink.commons.rest.response.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorConverter implements Converter<Errors, List<ValidationError>> {

    @Override
    public List<ValidationError> convert(Errors errors) {

        List<ValidationError> validationErrors = new ArrayList<>();

        for (ObjectError error : errors.getAllErrors()) {

            if (error instanceof FieldError) {

                FieldError fieldError = (FieldError) error;

                String message = fieldError.isBindingFailure()
                        ? "Field has an invalid value"
                        : error.getDefaultMessage();

                validationErrors.add(new ValidationError(fieldError.getField(), message));

            } else {

                validationErrors.add(new ValidationError(error.getDefaultMessage()));
            }

        }

        return validationErrors;
    }

}
