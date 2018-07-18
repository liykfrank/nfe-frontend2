package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt01FieldMappingTest extends RecordFieldMappingTestCase<RecordIt01> {

    private static final String RECORD_FILE_NAME = "IT01";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt01 record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT01));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getFileType(), isEmptyString());
        assertThat(record.getFileTypeSequenceNumber(), isEmptyString());
        assertThat(record.getHandbookRevisionNumber(), equalTo("203"));
        assertThat(record.getIsoCountryCode(), equalTo("AL"));
        assertThat(record.getProcessingDate(), equalTo("170412"));
        assertThat(record.getProcessingTime(), equalTo("0814"));
        assertThat(record.getReportingSystemIdentifier(), equalTo("MASS"));
        assertThat(record.getReportingSystemIdentifier(), equalTo("MASS"));
        assertThat(record.getSystemProviderReportingPeriodEndingDate(), equalTo("170415"));
        assertThat(record.getTestProductionStatus(), equalTo("TEST"));
    }

}
