package org.iata.bsplink.commons.rest.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ApplicationInternalServerErrorTest {

    private static final String MESSAGE = "any message";
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    @Test
    public void testCreatesExceptionWithMessage() {

        ApplicationInternalServerError exception = new ApplicationInternalServerError(MESSAGE);

        assertThat(exception.getMessage(), equalTo(MESSAGE));
        assertThat(exception.getStatus(), equalTo(HTTP_STATUS));
    }

    @Test
    public void testCreatesExceptionWithMessageAndCause() {

        RuntimeException cause = new RuntimeException();

        ApplicationInternalServerError exception =
                new ApplicationInternalServerError(MESSAGE, cause);

        assertThat(exception.getMessage(), equalTo(MESSAGE));
        assertThat(exception.getStatus(), equalTo(HTTP_STATUS));
        assertThat(exception.getCause(), sameInstance(cause));
    }
}
