package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.iata.bsplink.refund.loader.model.record.TransactionRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RefundDocumentValidatorTest {

    private RefundDocumentValidator validator;
    private List<RefundLoaderError> refundLoaderErrors;
    private RefundDocument refundDocument;

    @Before
    public void setUp() throws Exception {
        refundDocument = new RefundDocument();
        refundDocument.addRecordIt05(it05());
        refundDocument.addRecordIt05(it05());
        refundDocument.addRecordIt08(it08());
        refundDocument.addRecordIt08(it08());
        refundDocument.setRecordIt02(new RecordIt02());
        refundDocument.getRecordIt02().setTransactionCode("RFND");
        refundDocument.getRecordIt02().setTransactionNumber("001234");
        refundDocument.addRecordIt03(new RecordIt03());
        refundDocument.getRecordsIt03().get(0).setTransactionNumber("001234");

        refundLoaderErrors = new ArrayList<>();
        validator = new RefundDocumentValidator(refundLoaderErrors);
    }

    @Test
    public void testIsValid() {
        assertTrue(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, IsEmptyCollection.empty());
    }


    @Test
    public void testIsValidWithoutRecords() {
        assertTrue(validator.isValid(new RefundDocument()));
    }


    @Test
    public void testIsNotValidTransactionCode() {
        refundDocument.getRecordIt02().setTransactionCode("ACMA");
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo("transactionCode"));
        assertThat(refundLoaderError.getMessage(),
                equalTo(RefundDocumentValidator.INCORRECT_TRANSACTION_CODE));
    }


    @Test
    public void testIsNotValidWithTwoXlp() {
        refundDocument.getRecordsIt05().get(0).setCommissionType2("XLP");
        refundDocument.getRecordsIt05().get(0).setCommissionType3("XLP");
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo("commissionType3"));
        assertThat(refundLoaderError.getMessage(),
                equalTo(RefundDocumentValidator.XLP_ONLY_ONCE));
    }


    @Test
    @Parameters
    public void testIsNotValidIt05(int recordNumber, String field, String value, String message)
            throws Exception {
        BeanUtils.setProperty(refundDocument.getRecordsIt05().get(recordNumber), field, value);
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo(field));
        assertThat(refundLoaderError.getMessage(), equalTo(message));
    }


    @Test
    @Parameters
    public void testIsNotValidIt08(int recordNumber, String field, String value, String message)
            throws Exception {
        BeanUtils.setProperty(refundDocument.getRecordsIt08().get(recordNumber), field, value);
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo(field));
        assertThat(refundLoaderError.getMessage(), equalTo(message));
    }

    @Test
    @Parameters
    public void testIsNotValidIt08SecondFop(int recordNumber, String field, String value,
            String message) throws Exception {
        refundDocument.getRecordsIt08().get(recordNumber).setFormOfPaymentAmount2("12");
        BeanUtils.setProperty(refundDocument.getRecordsIt08().get(recordNumber), field, value);
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo(field));
        assertThat(refundLoaderError.getMessage(), equalTo(message));
    }

    @Test
    @Parameters
    public void testIsNotValidTransactionNumberingInIt02(String transactionNumber) {
        refundDocument.getRecordIt02().setTransactionNumber(transactionNumber);
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo("transactionNumber"));
        assertThat(refundLoaderError.getRecordIdentifier(), equalTo(RecordIdentifier.IT02));
        assertThat(refundLoaderError.getMessage(),
                equalTo(RefundDocumentValidator.INCORRECT_TRANSACTION_NUMBER));
    }


    @Test
    @Parameters(method = "parametersForTestsTransactionNumbering")
    public void testIsValidTransactionNumbering(TransactionRecord record) {
        record.setTransactionNumber(refundDocument.getRecordIt02().getTransactionNumber());
        switch (record.getRecordIdentifier().getIdentifier().charAt(0)) {
            case '3':
                refundDocument.addRecordIt03((RecordIt03) record);
                break;
            case '5':
                refundDocument.addRecordIt05((RecordIt05) record);
                break;
            case '8':
                refundDocument.addRecordIt08((RecordIt08) record);
                break;
            case 'H':
                refundDocument.addRecordIt0h((RecordIt0h) record);
                break;
            case 'Y':
                refundDocument.addRecordIt0y((RecordIt0y) record);
                break;
            default: return;
        }
        assertTrue(validator.isValid(refundDocument));
    }


    @Test
    @Parameters(method = "parametersForTestsTransactionNumbering")
    public void testIsNotValidTransactionNumbering(TransactionRecord record) {
        record.setTransactionNumber("001252");

        switch (record.getRecordIdentifier().getIdentifier().charAt(0)) {
            case '3':
                refundDocument.addRecordIt03((RecordIt03) record);
                break;
            case '5':
                refundDocument.addRecordIt05((RecordIt05) record);
                break;
            case '8':
                refundDocument.addRecordIt08((RecordIt08) record);
                break;
            case 'H':
                refundDocument.addRecordIt0h((RecordIt0h) record);
                break;
            case 'Y':
                refundDocument.addRecordIt0y((RecordIt0y) record);
                break;
            default: return;
        }
        assertFalse(validator.isValid(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        RefundLoaderError refundLoaderError = refundLoaderErrors.get(0);
        assertThat(refundLoaderError.getField(), equalTo("transactionNumber"));
        assertThat(refundLoaderError.getRecordIdentifier(), equalTo(record.getRecordIdentifier()));
        assertThat(refundLoaderError.getMessage(),
                equalTo(RefundDocumentValidator.INCORRECT_TRANSACTION_NUMBER));
    }


    /**
     * Parameters for Transaction Numbering test.
     */
    public Object[][] parametersForTestIsNotValidTransactionNumberingInIt02() {
        return new Object[][] {
            { "" },
            { "X" },
            { null }
        };
    }

    /**
     * Parameters for Transaction Numbering test.
     */
    public Object[][] parametersForTestsTransactionNumbering() {
        return new Object[][] {
            { new RecordIt03() },
            { it05() },
            { it08() },
            { new RecordIt0h() },
            { new RecordIt0y() }
        };
    }


    /**
     * Parameters for IT05 test.
     */
    public Object[][] parametersForTestIsNotValidIt05() {
        return new Object[][] {
            { 0, "currencyType", "", RefundDocumentValidator.MANDATORY },
            { 1, "currencyType", "", RefundDocumentValidator.MANDATORY },
            { 1, "currencyType", "USD2", RefundDocumentValidator.INCORRECT_CURRENCY },
            { 1, "ticketDocumentAmount", "1",
                RefundDocumentValidator.COMMISSION_TDAM_ON_FIRST_IT05 },
            { 1, "commissionAmount1", "1",
                RefundDocumentValidator.COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionAmount2", "1",
                RefundDocumentValidator.COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionAmount3", "1",
                RefundDocumentValidator.COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionRate1", "1", RefundDocumentValidator.COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionRate2", "1", RefundDocumentValidator.COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionRate3", "1", RefundDocumentValidator.COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionType1", "1", RefundDocumentValidator.FIRST_COMMISSION_TYPE_BLANK },
            { 1, "commissionType2", "1", RefundDocumentValidator.COMMISSION_TYPE_ON_FIRST_IT05 },
            { 1, "commissionType3", "1", RefundDocumentValidator.COMMISSION_TYPE_ON_FIRST_IT05 },
            { 0, "commissionType2", "PLX", RefundDocumentValidator.INVALID_COMMISSION_TYPE },
            { 0, "commissionType3", "PLX", RefundDocumentValidator.INVALID_COMMISSION_TYPE },
            { 0, "commissionType1", "XLP", RefundDocumentValidator.FIRST_COMMISSION_TYPE_BLANK }
        };
    }


    /**
     * Parameters for IT08 test.
     */
    public Object[][] parametersForTestIsNotValidIt08() {
        return new Object[][] {
            { 0, "currencyType1", "", RefundDocumentValidator.MANDATORY },
            { 1, "currencyType1", "", RefundDocumentValidator.MANDATORY },
            { 1, "currencyType1", "USD2", RefundDocumentValidator.INCORRECT_CURRENCY }
        };
    }

    /**
     * Parameters for IT08 test for second Form of Payment.
     */
    public Object[][] parametersForTestIsNotValidIt08SecondFop() {
        return new Object[][] {
            { 0, "currencyType2", "", RefundDocumentValidator.MANDATORY },
            { 1, "currencyType2", "", RefundDocumentValidator.MANDATORY },
            { 0, "currencyType2", "USD2", RefundDocumentValidator.INCORRECT_CURRENCY }
        };
    }

    private RecordIt05 it05() {
        RecordIt05 it05 = new RecordIt05();
        it05.setTransactionNumber("001234");
        it05.setCommissionRate1("0");
        it05.setCommissionRate2("0");
        it05.setCommissionRate3("0");
        it05.setCommissionAmount1("0");
        it05.setCommissionAmount2("0");
        it05.setCommissionAmount3("0");
        it05.setCommissionType1("");
        it05.setCommissionType2("");
        it05.setCommissionType3("");
        it05.setTicketDocumentAmount("0");
        it05.setCurrencyType("CAD2");
        return it05;
    }

    private RecordIt08 it08() {
        RecordIt08 it08 = new RecordIt08();
        it08.setTransactionNumber("001234");
        it08.setFormOfPaymentAmount1("0");
        it08.setFormOfPaymentAmount2("0");
        it08.setCurrencyType1("CAD2");
        it08.setCurrencyType2("");
        return it08;
    }
}
