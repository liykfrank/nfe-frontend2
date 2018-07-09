package org.iata.bsplink.refund.exception;

import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ActionNotAllowed extends ApplicationException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public ActionNotAllowed(String message) {
        super(message, STATUS);
    }

}
