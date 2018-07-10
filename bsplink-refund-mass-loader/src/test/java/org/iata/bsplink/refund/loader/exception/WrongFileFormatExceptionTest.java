package org.iata.bsplink.refund.loader.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WrongFileFormatExceptionTest {

    @Test
    public void testContructsExceptionWithMessage() {

        WrongFileFormatException exception = new WrongFileFormatException("foo");

        assertEquals("foo", exception.getMessage());
    }

}
