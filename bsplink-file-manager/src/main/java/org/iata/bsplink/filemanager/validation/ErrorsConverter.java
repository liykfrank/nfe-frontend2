package org.iata.bsplink.filemanager.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ErrorsConverter {

    private Errors errors;

    public ErrorsConverter(Errors errors) {

        this.errors = errors;
    }

    @Override
    public String toString() {

        String errorMessage = "";

        for (ObjectError error : errors.getAllErrors()) {

            if (error instanceof FieldError) {

                FieldError fieldError = (FieldError) error;

                if (fieldError.isBindingFailure()) {

                    errorMessage += fieldError.getField() + " has an invalid value";
                } else {

                    errorMessage += error.getDefaultMessage();
                }


            } else {

                errorMessage += error.getDefaultMessage();
            }

            errorMessage += ", ";
        }

        return errorMessage.replaceAll(",\\s$",  "");
    }

}
