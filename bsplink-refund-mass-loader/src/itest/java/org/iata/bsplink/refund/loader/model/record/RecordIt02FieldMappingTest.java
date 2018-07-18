package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt02FieldMappingTest extends RecordFieldMappingTestCase<RecordIt02> {

    private static final String RECORD_FILE_NAME = "IT02";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt02 record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT02));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getAgentNumericCode(), equalTo("23200903"));
        assertThat(record.getApprovedLocationNumericCode1(), equalTo("00000000"));
        assertThat(record.getApprovedLocationNumericCode2(), equalTo("00000000"));
        assertThat(record.getApprovedLocationNumericCode3(), equalTo("00000000"));
        assertThat(record.getApprovedLocationType1(), isEmptyString());
        assertThat(record.getApprovedLocationType2(), isEmptyString());
        assertThat(record.getApprovedLocationType3(), isEmptyString());
        assertThat(record.getApprovedLocationType4(), isEmptyString());
        assertThat(record.getCheckDigit1(), equalTo("9"));
        assertThat(record.getCheckDigit2(), equalTo("4"));
        assertThat(record.getConjunctionTicketIndicator(), isEmptyString());
        assertThat(record.getCouponUseIndicator(), isEmptyString());
        assertThat(record.getDataInputStatusIndicator(), equalTo("M"));
        assertThat(record.getDateOfIssue(), equalTo("170412"));
        assertThat(record.getFormatIdentifier(), isEmptyString());
        assertThat(record.getStockSetStockControlNumberFrom1(), isEmptyString());// TODO: name?
        assertThat(record.getStockSetStockControlNumberFrom2(), isEmptyString());
        assertThat(record.getStockSetStockControlNumberFrom3(), isEmptyString());
        assertThat(record.getStockSetStockControlNumberTo1(), equalTo("0000"));
        assertThat(record.getStockSetStockControlNumberTo2(), equalTo("0000"));
        assertThat(record.getStockSetStockControlNumberTo3(), equalTo("0000"));
        assertThat(record.getStockSetStockControlNumberTo4(), equalTo("0000"));
        assertThat(record.getIsoCountryCode(), equalTo("AL"));
        assertThat(record.getPassengerName(), equalTo("SDFX"));
        assertThat(record.getRefundApplicationStatus(), equalTo("A"));
        assertThat(record.getSettlementAuthorisationCode(), isEmptyString());
        assertThat(record.getStatisticalCode(), equalTo("I"));
        assertThat(record.getStockControlNumber(), isEmptyString());
        assertThat(record.getTicketDocumentNumber(), equalTo("0745200000047"));
        assertThat(record.getTicketingAirlineCodeNumber(), equalTo("074"));
        assertThat(record.getTransactionCode(), equalTo("RFND")); // TODO: check if it's correct
        assertThat(record.getTransactionNumber(), equalTo("000001"));
        assertThat(record.getTransmissionControlNumber(), isEmptyString());
        assertThat(record.getTransmissionControlNumberCheckDigit(), equalTo("0"));
        assertThat(record.getVendorIdentification(), isEmptyString());
        assertThat(record.getVendorIsoCountryCode(), isEmptyString()); //TODO: check if it's correct

    }

}
