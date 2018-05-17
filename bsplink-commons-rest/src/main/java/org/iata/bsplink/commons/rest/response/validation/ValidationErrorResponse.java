package org.iata.bsplink.commons.rest.response.validation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.iata.bsplink.commons.rest.response.ApplicationErrorResponse;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ValidationErrorResponse extends ApplicationErrorResponse {

    private List<ValidationError> validationErrors = new ArrayList<>();

    /**
     * Validation error response body.
     */
    public ValidationErrorResponse(String message, HttpStatus httpStatus, String path,
            List<ValidationError> validationErrors) {

        super(message, httpStatus, path);
        this.validationErrors = validationErrors;
    }
}
