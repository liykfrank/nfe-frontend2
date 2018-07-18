package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt0zFieldMappingTest extends RecordFieldMappingTestCase<RecordIt0z> {

    private static final String RECORD_FILE_NAME = "IT0Z";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt0z record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT0Z));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getReportRecordCounter(), equalTo("00000000008"));

    }

}
