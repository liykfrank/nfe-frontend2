package org.iata.bsplink.commons.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Specific internal server error exception.
 */
public class ApplicationInternalServerError extends ApplicationException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public ApplicationInternalServerError(String message, Throwable cause) {
        super(message, HTTP_STATUS, cause);
    }

    public ApplicationInternalServerError(String message) {
        super(message, HTTP_STATUS);
    }

}
