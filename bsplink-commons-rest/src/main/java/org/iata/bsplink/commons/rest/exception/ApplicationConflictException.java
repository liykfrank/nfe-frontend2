package org.iata.bsplink.commons.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Represents a conflict with the current state of the application.
 *
 * <p>
 * Suitable for situations in which the user sends values with some kind of conflict with
 * values already saved in the application, for example when the user tries to add an e-mail
 * which already exists.
 * </p>
 */
public class ApplicationConflictException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationConflictException(String message) {

        super(message, HttpStatus.CONFLICT);
    }

}
