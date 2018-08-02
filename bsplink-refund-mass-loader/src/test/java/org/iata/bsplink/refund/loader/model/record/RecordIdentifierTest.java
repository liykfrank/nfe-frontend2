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

    @Test
    public void testHasExpectedLayout() {

        assertTrue(RecordIdentifier.IT01.getLayout() instanceof RecordIt01Layout);
        assertTrue(RecordIdentifier.IT02.getLayout() instanceof RecordIt02Layout);
        assertTrue(RecordIdentifier.IT03.getLayout() instanceof RecordIt03Layout);
        assertTrue(RecordIdentifier.IT05.getLayout() instanceof RecordIt05Layout);
        assertTrue(RecordIdentifier.IT08.getLayout() instanceof RecordIt08Layout);
        assertTrue(RecordIdentifier.IT0H.getLayout() instanceof RecordIt0hLayout);
        assertTrue(RecordIdentifier.IT0Y.getLayout() instanceof RecordIt0yLayout);
        assertTrue(RecordIdentifier.IT0Z.getLayout() instanceof RecordIt0zLayout);
        assertTrue(RecordIdentifier.UNKNOWN.getLayout() instanceof RecordRawLineLayout);
    }

}
