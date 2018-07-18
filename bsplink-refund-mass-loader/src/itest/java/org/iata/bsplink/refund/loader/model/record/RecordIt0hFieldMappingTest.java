package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt0hFieldMappingTest extends RecordFieldMappingTestCase<RecordIt0h> {

    private static final String RECORD_FILE_NAME = "IT0H";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt0h record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT0H));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getReasonForMemoInformation1(),
                equalTo("Rejected: Duplicated refund application reque"));
        assertThat(record.getReasonForMemoInformation2(), equalTo("st otra razon mas de vueling"));
        assertThat(record.getReasonForMemoInformation3(), isEmptyString());
        assertThat(record.getReasonForMemoInformation4(), isEmptyString());
        assertThat(record.getReasonForMemoInformation5(), isEmptyString());
        assertThat(record.getReasonForMemoIssuanceCode(), isEmptyString());
        assertThat(record.getReasonForMemoLineIdentifier1(), equalTo("001"));
        assertThat(record.getReasonForMemoLineIdentifier2(), equalTo("002"));
        assertThat(record.getReasonForMemoLineIdentifier3(), equalTo("003"));
        assertThat(record.getReasonForMemoLineIdentifier4(), equalTo("004"));
        assertThat(record.getReasonForMemoLineIdentifier5(), equalTo("005"));
        assertThat(record.getTransactionNumber(), equalTo("000002"));

    }

}
