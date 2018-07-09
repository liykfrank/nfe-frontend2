package org.iata.bsplink.refund.exception;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ActionNotAllowedTest {

    @Test
    public void testHasExpectedMessage() {

        ActionNotAllowed exception = new ActionNotAllowed("foo");

        assertThat(exception.getMessage(), equalTo("foo"));
    }

}
