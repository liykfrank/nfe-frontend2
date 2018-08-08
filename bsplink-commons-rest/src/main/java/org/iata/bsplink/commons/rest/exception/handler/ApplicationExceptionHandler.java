package org.iata.bsplink.commons.rest.exception.handler;

import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.iata.bsplink.commons.rest.response.ApplicationErrorResponse;
import org.iata.bsplink.commons.rest.response.ApplicationErrorResponseBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Logs all the exceptions and converts them to a more suitable response.
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private ApplicationErrorResponseBuilder responseBuilder;

    public ApplicationExceptionHandler(ApplicationErrorResponseBuilder responseBuilder) {

        this.responseBuilder = responseBuilder;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApplicationErrorResponse response = responseBuilder.build(request, status, ex);

        ResponseEntity<Object> responseEntity =
                super.handleExceptionInternal(ex, response, headers, status, request);

        logResponse(responseEntity, ex);

        return responseEntity;
    }

    private void logResponse(ResponseEntity<Object> response, Exception ex) {

        if (response.getStatusCode().is5xxServerError() && logger.isErrorEnabled()) {

            logger.error(response, ex);

        } else if (logger.isInfoEnabled()) {

            logger.info(response, ex);
        }
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApiException(ApplicationException ex, WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(), ex.getStatus(), request);
    }

    /**
     * Handles all exceptions not captured yet and converts them to a generic internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles all denied exceptions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccesDeniedException(AccessDeniedException ex,
            WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

}
