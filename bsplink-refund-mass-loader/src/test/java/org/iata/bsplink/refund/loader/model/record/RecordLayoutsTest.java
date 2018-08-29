package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordLayoutsTest {

    @Test
    public void testGetsRecordLayout() {

        assertThat(RecordLayouts.get(RecordIdentifier.IT01), instanceOf(RecordIt01Layout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT02), instanceOf(RecordIt02Layout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT03), instanceOf(RecordIt03Layout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT05), instanceOf(RecordIt05Layout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT08), instanceOf(RecordIt08Layout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT0Y), instanceOf(RecordIt0yLayout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT0H), instanceOf(RecordIt0hLayout.class));
        assertThat(RecordLayouts.get(RecordIdentifier.IT0Z), instanceOf(RecordIt0zLayout.class));
    }

}
