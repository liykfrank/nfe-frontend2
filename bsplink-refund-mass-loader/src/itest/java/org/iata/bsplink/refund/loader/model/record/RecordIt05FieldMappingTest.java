package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt05FieldMappingTest extends RecordFieldMappingTestCase<RecordIt05> {

    private static final String RECORD_FILE_NAME = "IT05";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt05 record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT05));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getAmountEnteredByAgent(), equalTo("00000000000"));
        assertThat(record.getAmountPaidByCustomer(), equalTo("00000000000"));
        assertThat(record.getCommissionAmount1(), equalTo("00000000000"));
        assertThat(record.getCommissionAmount2(), equalTo("00000000000"));
        assertThat(record.getCommissionAmount3(), equalTo("00000000000"));
        assertThat(record.getCommissionRate1(), equalTo("00000"));
        assertThat(record.getCommissionRate2(), equalTo("00000"));
        assertThat(record.getCommissionRate3(), equalTo("00000"));
        assertThat(record.getCommissionType1(), isEmptyString());
        assertThat(record.getCommissionType2(), isEmptyString());
        assertThat(record.getCommissionType3(), isEmptyString());
        assertThat(record.getCurrencyType(), equalTo("EUR2"));
        assertThat(record.getNetReportingIndicator(), isEmptyString());
        assertThat(record.getTaxOnCommissionAmount(), equalTo("00000000000"));
        assertThat(record.getTaxOnCommissionType(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeAmount1(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeAmount2(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeAmount3(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeAmount4(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeAmount5(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeAmount6(), equalTo("00000000000"));
        assertThat(record.getTaxMiscellaneousFeeType1(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeType2(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeType3(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeType4(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeType5(), isEmptyString());
        assertThat(record.getTaxMiscellaneousFeeType6(), isEmptyString());
        assertThat(record.getTicketDocumentAmount(), equalTo("00000150000"));
        assertThat(record.getTransactionNumber(), equalTo("000001"));

    }

}
