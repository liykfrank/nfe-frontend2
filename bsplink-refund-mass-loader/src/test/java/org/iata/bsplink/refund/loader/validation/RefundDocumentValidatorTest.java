package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.iata.bsplink.refund.loader.utils.BeanPropertyUtils.setProperty;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.COMMISSION_AMOUNT_ON_FIRST_IT05;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.COMMISSION_RATE_ON_FIRST_IT05;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.COMMISSION_TDAM_ON_FIRST_IT05;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.COMMISSION_TYPE_ON_FIRST_IT05;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.FIRST_COMMISSION_TYPE_BLANK;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.INCORRECT_CURRENCY;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.INCORRECT_RECORD_ORDER;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.INCORRECT_TRANSACTION_CODE;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.INCORRECT_TRANSACTION_NUMBER;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.INVALID_COMMISSION_TYPE;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.MANDATORY;
import static org.iata.bsplink.refund.loader.validation.RefundDocumentValidator.XLP_ONLY_ONCE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
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
        lineNumberRecords();
        refundLoaderErrors = new ArrayList<>();
        validator = new RefundDocumentValidator();
    }

    @Test
    public void testIsValid() {

        assertTrue(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, IsEmptyCollection.empty());
    }

    private boolean executeValidation(RefundDocument refundDocument) {

        return validator.validate(refundDocument, refundLoaderErrors);
    }

    @Test
    public void testIsValidWithoutRecords() {

        assertTrue(validator.validate(new RefundDocument(), refundLoaderErrors));
    }


    @Test
    public void testIsNotValidTransactionCode() {

        RecordIt02 it02 = refundDocument.getRecordIt02();
        it02.setTransactionCode("ACMA");
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it02, "transactionCode", INCORRECT_TRANSACTION_CODE)));
    }


    @Test
    public void testIsNotValidWithTwoXlp() {

        RecordIt05 it05 = refundDocument.getRecordsIt05().get(0);
        it05.setCommissionType2("XLP");
        it05.setCommissionType3("XLP");
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it05, "commissionType3", XLP_ONLY_ONCE)));
    }


    @Test
    @Parameters
    public void testIsNotValidIt05(int recordNumber, String field, String value, String message)
            throws Exception {

        RecordIt05 it05 = refundDocument.getRecordsIt05().get(recordNumber);
        setProperty(it05, field, value);
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it05, field, message)));
    }

    @Test
    @Parameters
    public void testIsNotValidIt08(int recordNumber, String field, String value, String message)
            throws Exception {

        RecordIt08 it08 = refundDocument.getRecordsIt08().get(recordNumber);
        setProperty(it08, field, value);
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it08, field, message)));
    }

    @Test
    @Parameters
    public void testIsNotValidIt08SecondFop(int recordNumber, String field, String value,
            String message) throws Exception {

        RecordIt08 it08 = refundDocument.getRecordsIt08().get(recordNumber);
        it08.setFormOfPaymentAmount2("12");
        setProperty(refundDocument.getRecordsIt08().get(recordNumber), field, value);
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it08, field, message)));
    }

    @Test
    @Parameters
    public void testIsNotValidTransactionNumberingInIt02(String transactionNumber) {

        RecordIt02 it02 = refundDocument.getRecordIt02();
        it02.setTransactionNumber(transactionNumber);
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(
                refundLoaderError(it02, "transactionNumber", INCORRECT_TRANSACTION_NUMBER)));
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

        lineNumberRecords();
        assertTrue(executeValidation(refundDocument));
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

        lineNumberRecords();
        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(1));

        RefundLoaderError expectedError = refundLoaderError(record, "transactionNumber",
                INCORRECT_TRANSACTION_NUMBER);
        expectedError.setTransactionNumber(refundDocument.getRecordIt02().getTransactionNumber());

        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(expectedError));
    }


    @Test
    public void testIsValidRecordOrden() {

        RecordIt03 it03 = new RecordIt03();
        refundDocument.addRecordIt03(it03);

        it03.setTransactionNumber(refundDocument.getRecordIt02().getTransactionNumber());

        lineNumberRecords();

        int lineNumberIt03 = it03.getLineNumber();
        RecordIt05 it05 = refundDocument.getRecordsIt05().get(0);
        int lineNumberIt05 = it05.getLineNumber();

        it03.setLineNumber(lineNumberIt05);
        it05.setLineNumber(lineNumberIt03);

        assertFalse(executeValidation(refundDocument));
        assertThat(refundLoaderErrors, hasSize(2));

        RefundLoaderError expectedIt03Error =
                refundLoaderError(it03, "recordIdentifier", INCORRECT_RECORD_ORDER);
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(expectedIt03Error));

        RefundLoaderError expectedIt05Error =
                refundLoaderError(it05, "recordIdentifier", INCORRECT_RECORD_ORDER);
        assertThat(refundLoaderErrors.get(1), samePropertyValuesAs(expectedIt05Error));
    }


    private void lineNumberRecords() {

        int lineNumber = 10;
        refundDocument.getRecordIt02().setLineNumber(lineNumber);

        List<Record> records = new ArrayList<>();
        records.addAll(refundDocument.getRecordsIt03());
        records.addAll(refundDocument.getRecordsIt05());
        records.addAll(refundDocument.getRecordsIt08());
        records.addAll(refundDocument.getRecordsIt0y());
        records.addAll(refundDocument.getRecordsIt0h());

        for (Record record: records) {

            record.setLineNumber(++lineNumber);
        }
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

            { 0, "currencyType", "", MANDATORY },
            { 1, "currencyType", "", MANDATORY },
            { 1, "currencyType", "USD2", INCORRECT_CURRENCY },
            { 1, "ticketDocumentAmount", "1",
                COMMISSION_TDAM_ON_FIRST_IT05 },
            { 1, "commissionAmount1", "1",
                COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionAmount2", "1",
                COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionAmount3", "1",
                COMMISSION_AMOUNT_ON_FIRST_IT05 },
            { 1, "commissionRate1", "1", COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionRate2", "1", COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionRate3", "1", COMMISSION_RATE_ON_FIRST_IT05 },
            { 1, "commissionType1", "1", FIRST_COMMISSION_TYPE_BLANK },
            { 1, "commissionType2", "1", COMMISSION_TYPE_ON_FIRST_IT05 },
            { 1, "commissionType3", "1", COMMISSION_TYPE_ON_FIRST_IT05 },
            { 0, "commissionType2", "PLX", INVALID_COMMISSION_TYPE },
            { 0, "commissionType3", "PLX", INVALID_COMMISSION_TYPE },
            { 0, "commissionType1", "XLP", FIRST_COMMISSION_TYPE_BLANK }
        };
    }


    /**
     * Parameters for IT08 test.
     */
    public Object[][] parametersForTestIsNotValidIt08() {

        return new Object[][] {

            { 0, "currencyType1", "", MANDATORY },
            { 1, "currencyType1", "", MANDATORY },
            { 1, "currencyType1", "USD2", INCORRECT_CURRENCY }
        };
    }

    /**
     * Parameters for IT08 test for second Form of Payment.
     */
    public Object[][] parametersForTestIsNotValidIt08SecondFop() {

        return new Object[][] {

            { 0, "currencyType2", "", MANDATORY },
            { 1, "currencyType2", "", MANDATORY },
            { 0, "currencyType2", "USD2", INCORRECT_CURRENCY }
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


    private RefundLoaderError refundLoaderError(TransactionRecord record, String field,
            String message) {

        RefundLoaderError error = new RefundLoaderError();
        error.setRecordIdentifier(record.getRecordIdentifier());
        error.setField(field);
        error.setLineNumber(record.getLineNumber());
        error.setMessage(message);
        error.setTransactionNumber(record.getTransactionNumber());
        error.setValidationPhase(ValidationPhase.TRANSACTION);
        return error;
    }

}
