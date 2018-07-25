package org.iata.bsplink.agencymemo.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class FeignClientExceptionTest {

    @Test
    public void testGetHttpStatus() {

        HttpStatus httpStatus = HttpStatus.FAILED_DEPENDENCY;
        FeignClientException e = new FeignClientException(httpStatus);

        assertThat(e.getHttpStatus(), equalTo(httpStatus));
    }

}
