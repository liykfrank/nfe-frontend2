package org.iata.bsplink.refund.loader.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ValidationError {

    private String fieldName;
    private String message;

    public ValidationError() {
        // default constructor
    }

    /**
     * Creates a validation error without field.
     */
    public ValidationError(String message) {

        this.message = message;
    }

    /**
     * Creates a validation error for a field.
     */
    public ValidationError(String fieldName, String message) {

        this.fieldName = fieldName;
        this.message = message;
    }
}
