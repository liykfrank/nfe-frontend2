package org.iata.bsplink.refund.loader.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WrongRecordCounterExceptionTest {

    @Test
    public void testConstructsExceptionWithExpectedMessage() {

        WrongRecordCounterException exception = new WrongRecordCounterException(1, 2);

        assertEquals("wrong number of file lines: expected 2 there are 1", exception.getMessage());
    }

}
