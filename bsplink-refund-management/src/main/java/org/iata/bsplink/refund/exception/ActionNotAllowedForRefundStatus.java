package org.iata.bsplink.refund.exception;

import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ActionNotAllowedForRefundStatus extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "action not allowed for refund status";

    public ActionNotAllowedForRefundStatus() {
        super(MESSAGE, STATUS);
    }

}
