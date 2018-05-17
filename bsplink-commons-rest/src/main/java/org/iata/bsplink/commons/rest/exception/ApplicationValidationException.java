package org.iata.bsplink.commons.rest.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

@Getter
public class ApplicationValidationException extends ApplicationException {

    private static final String MESSAGE = "Validation error";

    private static final long serialVersionUID = 1L;

    private Errors errors;

    /**
     * Creates the exception with an Errors.
     */
    public ApplicationValidationException(Errors errors) {

        super(MESSAGE, HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }
}
