package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FieldLayoutTest {

    @Test
    public void testGetRanges() {

        FieldLayout fieldLayout = new FieldLayout(RecordIdentifier.IT01, "handbookRevisionNumber",
                4, "REVN", FieldType.N, 12, 3);

        assertThat(fieldLayout.getRange().getMin(), equalTo(12));
        assertThat(fieldLayout.getRange().getMax(), equalTo(14));
    }

}
