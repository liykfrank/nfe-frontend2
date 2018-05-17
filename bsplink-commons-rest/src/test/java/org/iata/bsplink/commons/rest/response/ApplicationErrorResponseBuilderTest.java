package org.iata.bsplink.commons.rest.response;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.commons.rest.response.validation.ValidationError;
import org.iata.bsplink.commons.rest.response.validation.ValidationErrorConverter;
import org.iata.bsplink.commons.rest.response.validation.ValidationErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.ServletWebRequest;

public class ApplicationErrorResponseBuilderTest {

    private static final String URI = "/foo";

    private long timestampReference;
    private HttpStatus status;
    private String message;
    private ApplicationErrorResponse response;
    private ServletWebRequest request;
    private ApplicationErrorResponseBuilder builder;
    private RuntimeException exception;
    private ValidationErrorConverter validationErrorConverter;

    @Before
    public void setUp() {

        this.timestampReference = Instant.now().toEpochMilli();

        status = HttpStatus.NOT_FOUND;
        message = HttpStatus.NOT_FOUND.getReasonPhrase();

        validationErrorConverter = mock(ValidationErrorConverter.class);

        request = createRequest(URI);
        builder = new ApplicationErrorResponseBuilder(validationErrorConverter);

        exception = new RuntimeException(message);

        response = builder.build(request, status, exception);
    }

    private ServletWebRequest createRequest(String uri) {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        return new ServletWebRequest(httpServletRequest);
    }

    @Test
    public void testBodyHasExpectedContent() {

        assertThat(response.getTimestamp(), greaterThanOrEqualTo(timestampReference));
        assertEquals(status.value(), response.getStatus());
        assertEquals(status.getReasonPhrase(), response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(URI, response.getPath());
    }

    @Test
    public void testAddsQueryStringIfItExists() {

        when(request.getRequest().getQueryString()).thenReturn("bar=a&baz=b");

        ApplicationErrorResponse response = builder.build(request, status, exception);

        String expected = URI + "?bar=a&baz=b";

        assertEquals(expected, response.getPath());
    }

    @Test
    public void testAddsGenericMessageOnServerError() {

        ApplicationErrorResponse response =
                builder.build(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), response.getMessage());
    }

    @Test
    public void testBuildsValidationErrorResponse() {

        Errors errors = mock(Errors.class);

        List<ValidationError> validationErrors = new ArrayList<>();

        when(validationErrorConverter.convert(errors)).thenReturn(validationErrors);

        ApplicationValidationException exception = new ApplicationValidationException(errors);

        ValidationErrorResponse response = (ValidationErrorResponse) builder.build(request,
                HttpStatus.BAD_REQUEST, exception);

        assertThat(response.getValidationErrors(), sameInstance(validationErrors));

        verify(validationErrorConverter).convert(errors);
    }

}
