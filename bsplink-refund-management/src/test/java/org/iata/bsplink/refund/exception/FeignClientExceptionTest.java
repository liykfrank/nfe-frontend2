package org.iata.bsplink.refund.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class FeignClientExceptionTest {

    @Test
    public void testHasExpectedStatus() {

        FeignClientException exception = new FeignClientException(HttpStatus.I_AM_A_TEAPOT);

        assertThat(exception.getHttpStatus(), equalTo(HttpStatus.I_AM_A_TEAPOT));
    }

}
