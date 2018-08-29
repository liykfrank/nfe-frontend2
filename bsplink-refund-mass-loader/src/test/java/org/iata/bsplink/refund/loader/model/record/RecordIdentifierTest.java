package org.iata.bsplink.refund.loader.model.record;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RecordIdentifierTest {

    @Test
    public void testMatches() {

        List<RecordIdentifier> values = Arrays.asList(RecordIdentifier.values());

        for (RecordIdentifier value : values) {

            assertTrue(value.matches(value.getIdentifier()));
        }
    }

    @Test
    public void testDoesNotMatches() {

        assertFalse(RecordIdentifier.IT01.matches("0"));
    }

}
