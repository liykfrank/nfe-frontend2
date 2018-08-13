package org.iata.bsplink.exception;

import lombok.Getter;

@Getter
public class CustomValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public CustomValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
