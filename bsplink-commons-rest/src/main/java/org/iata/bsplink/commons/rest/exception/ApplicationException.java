package org.iata.bsplink.commons.rest.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    /**
     * Application exception with associated HTTP status code.
     */
    public ApplicationException(String message, HttpStatus status) {

        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {

        return status;
    }

}
