package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordIt08FieldMappingTest extends RecordFieldMappingTestCase<RecordIt08> {

    private static final String RECORD_FILE_NAME = "IT08";

    @Override
    String getRecordFileName() {

        return RECORD_FILE_NAME;
    }

    @Override
    @Test
    public void testFieldsAreMappedCorrectly() throws Exception {

        RecordIt08 record = readRecord();

        assertThat(record.getRecordIdentifier(), equalTo(RecordIdentifier.IT08));
        assertThat(record.getLineNumber(), equalTo(1));
        assertThat(record.getAddressVerificationCode1(), isEmptyString());
        assertThat(record.getAddressVerificationCode2(), isEmptyString());
        assertThat(record.getApprovalCode1(), isEmptyString());
        assertThat(record.getApprovalCode2(), isEmptyString());
        assertThat(record.getAuthorisedAmount1(), isEmptyString());
        assertThat(record.getAuthorisedAmount2(), isEmptyString());
        assertThat(record.getCreditCardCorporateContract1(), isEmptyString());
        assertThat(record.getCreditCardCorporateContract2(), isEmptyString());
        assertThat(record.getCurrencyType1(), equalTo("EUR2"));
        assertThat(record.getCurrencyType2(), equalTo("EUR2"));
        assertThat(record.getCustomerFileReference1(), isEmptyString());
        assertThat(record.getCustomerFileReference2(), isEmptyString());
        assertThat(record.getExpiryDate1(), isEmptyString());
        assertThat(record.getExpiryDate2(), isEmptyString());
        assertThat(record.getExtendedPaymentCode1(), isEmptyString());
        assertThat(record.getExtendedPaymentCode2(), isEmptyString());
        assertThat(record.getFormOfPaymentAccountNumber1(), equalTo("37601000O0000GB"));
        assertThat(record.getFormOfPaymentAccountNumber2(), equalTo("45648000b00000eP"));
        assertThat(record.getFormOfPaymentAmount1(), equalTo("00000030000"));
        assertThat(record.getFormOfPaymentAmount2(), equalTo("00000040000"));
        assertThat(record.getFormOfPaymentType1(), equalTo("CCAX3760"));
        assertThat(record.getFormOfPaymentType2(), equalTo("EPVI4564"));
        assertThat(record.getFormOfPaymentTransactionIdentifier1(), isEmptyString());
        assertThat(record.getFormOfPaymentTransactionIdentifier2(), isEmptyString());
        assertThat(record.getSourceOfApprovalCode1(), isEmptyString());
        assertThat(record.getSourceOfApprovalCode2(), isEmptyString());
        assertThat(record.getTransactionNumber(), equalTo("000001"));

    }

}
