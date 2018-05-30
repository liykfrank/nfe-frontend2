package org.iata.bsplink.commons.rest.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ApplicationExceptionTest {

    private static final String MESSAGE = "any message";
    private static final HttpStatus HTTP_STATUS = HttpStatus.I_AM_A_TEAPOT;

    @Test
    public void testCreatesExceptionWithMessageAndHttpStatusCode() {

        ApplicationException exception = new ApplicationException(MESSAGE, HTTP_STATUS);

        assertThat(exception.getMessage(), equalTo(MESSAGE));
        assertThat(exception.getStatus(), equalTo(HTTP_STATUS));
    }

    @Test
    public void testCreatesExceptionWithMessageAndHttpStatusCodeAndCause() {

        RuntimeException cause = new RuntimeException();

        ApplicationException exception = new ApplicationException(MESSAGE, HTTP_STATUS, cause);

        assertThat(exception.getMessage(), equalTo(MESSAGE));
        assertThat(exception.getStatus(), equalTo(HTTP_STATUS));
        assertThat(exception.getCause(), sameInstance(cause));
    }

}
