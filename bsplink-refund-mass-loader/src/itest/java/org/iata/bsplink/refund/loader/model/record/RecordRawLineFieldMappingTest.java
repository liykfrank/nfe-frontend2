package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordRawLineFieldMappingTest extends RecordFieldMappingTestCase<RecordRawLine> {

    private static final String RECORD_FILE_NAME = "UNKNOWN";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordRawLine record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.UNKNOWN));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getLine(), equalTo("any value"));

    }

}
