package org.iata.bsplink.commons.rest.response;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.commons.rest.response.validation.ValidationError;
import org.iata.bsplink.commons.rest.response.validation.ValidationErrorConverter;
import org.iata.bsplink.commons.rest.response.validation.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class ApplicationErrorResponseBuilder {

    private ValidationErrorConverter validationErrorConverter;

    public ApplicationErrorResponseBuilder(ValidationErrorConverter validationErrorConverter) {

        this.validationErrorConverter = validationErrorConverter;
    }

    /**
     * Builds a response with information about the error.
     */
    public ApplicationErrorResponse build(WebRequest request, HttpStatus status,
            Exception exception) {

        String path = extractPath(request);

        if (exception instanceof ApplicationValidationException) {

            return buildValidationErrorResponse(exception.getMessage(), status, path,
                    (ApplicationValidationException) exception);

        } else if (exception instanceof ApplicationException) {

            return new ApplicationErrorResponse(exception.getMessage(), status, path);
        }

        return new ApplicationErrorResponse(status.getReasonPhrase(), status, path);
    }

    /**
     * Extracts the request path and appends the query string if exists.
     */
    private String extractPath(WebRequest request) {

        HttpServletRequest httpServletRequest = ((ServletWebRequest)request).getRequest();

        String path = httpServletRequest.getRequestURI();

        if (httpServletRequest.getQueryString() != null) {

            path += "?" + httpServletRequest.getQueryString();
        }

        return path;
    }

    private ValidationErrorResponse buildValidationErrorResponse(String message, HttpStatus status,
            String path, ApplicationValidationException exception) {

        List<ValidationError> errors = validationErrorConverter.convert(exception.getErrors());

        return new ValidationErrorResponse(message, status, path, errors);
    }

}
