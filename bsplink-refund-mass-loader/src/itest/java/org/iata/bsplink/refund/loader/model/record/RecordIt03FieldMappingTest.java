package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt03FieldMappingTest extends RecordFieldMappingTestCase<RecordIt03> {

    @Override
    String getRecordFileName() {

        return "IT03_ALe9EARS_20170410_0744_016";
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt03 record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT03));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getCheckDigit1(), equalTo("9"));
        assertThat(record.getCheckDigit2(), equalTo("0"));
        assertThat(record.getCheckDigit3(), equalTo("0"));
        assertThat(record.getCheckDigit4(), equalTo("0"));
        assertThat(record.getCheckDigit5(), equalTo("0"));
        assertThat(record.getCheckDigit6(), equalTo("0"));
        assertThat(record.getCheckDigit7(), equalTo("0"));
        assertThat(record.getDateOfIssueRelatedDocument(), equalTo("170404"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier1(), equalTo("1234"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier2(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier3(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier4(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier5(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier6(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentCouponNumberIdentifier7(), equalTo("0000"));
        assertThat(record.getRelatedTicketDocumentNumber1(), equalTo("0749999999999"));
        assertThat(record.getRelatedTicketDocumentNumber2(), isEmptyString());
        assertThat(record.getRelatedTicketDocumentNumber3(), isEmptyString());
        assertThat(record.getRelatedTicketDocumentNumber4(), isEmptyString());
        assertThat(record.getRelatedTicketDocumentNumber5(), isEmptyString());
        assertThat(record.getRelatedTicketDocumentNumber6(), isEmptyString());
        assertThat(record.getRelatedTicketDocumentNumber7(), isEmptyString());
        assertThat(record.getTourCode(), isEmptyString());
        assertThat(record.getTransactionNumber(), equalTo("000001"));
        assertThat(record.getWaiverCode(), equalTo("88888888888888"));

    }

}
