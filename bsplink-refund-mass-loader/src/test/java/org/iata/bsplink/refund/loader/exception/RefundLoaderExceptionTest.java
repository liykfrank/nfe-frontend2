package org.iata.bsplink.refund.loader.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RefundLoaderExceptionTest {

    @Test
    public void testContructsExceptionWithMessage() {

        RefundLoaderException exception = new RefundLoaderException("foo");

        assertEquals("foo", exception.getMessage());
    }

}
