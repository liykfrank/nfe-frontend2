package org.iata.bsplink.commons.rest.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

public class ApplicationValidationExceptionTest {

    private ApplicationValidationException exception;
    private Errors errors;

    @Before
    public void setUp() {

        mock(Errors.class);
        exception = new ApplicationValidationException(errors);
    }

    @Test
    public void testHasBadBadRequestStatus() {

        assertThat(exception.getStatus(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testHasExpectedMessage() {

        assertThat(exception.getMessage(), equalTo("Validation error"));
    }

    @Test
    public void testGetErrors() {

        assertThat(exception.getErrors(), equalTo(errors));
    }

}
