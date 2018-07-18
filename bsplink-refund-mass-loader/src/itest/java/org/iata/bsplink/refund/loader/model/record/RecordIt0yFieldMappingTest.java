package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt0yFieldMappingTest extends RecordFieldMappingTestCase<RecordIt0y> {

    private static final String RECORD_FILE_NAME = "IT0Y";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt0y record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT0Y));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getTransactionNumber(), equalTo("000001"));

    }

}
