package org.iata.bsplink.refund.loader.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.TransactionRecord;
import org.springframework.stereotype.Component;

@Component
public class RefundDocumentValidator {

    public static final String INCORRECT_TRANSACTION_CODE = "Incorrect transaction code";
    public static final String INVALID_COMMISSION_TYPE = "Invalid Commission Type";
    public static final String MANDATORY = "The field is mandatory";
    public static final String INCORRECT_CURRENCY = "Incorrect Currency Type";
    public static final String INCORRECT_TRANSACTION_NUMBER = "Incorrect Transaction Number";
    public static final String COMMISSION_AMOUNT_ON_FIRST_IT05 =
            "Commission Amount only has to be reported on the first IT05";
    public static final String COMMISSION_RATE_ON_FIRST_IT05 =
            "Commission Rate only has to be reported on the first IT05";
    public static final String COMMISSION_TYPE_ON_FIRST_IT05 =
            "Commission Type only has to be reported on the first IT05";
    public static final String COMMISSION_TDAM_ON_FIRST_IT05 =
           "Ticket/Document Amount only has to be reported on the first IT05";
    public static final String FIRST_COMMISSION_TYPE_BLANK =
            "First Commission Type has to be left blank";
    public static final String XLP_ONLY_ONCE =
            "XLP Commission Type can only be reported once";
    public static final String INCORRECT_ORDEN = "The record is not reported in the correct order.";

    private static final String CURRENCY_TYPE = "currencyType";
    private static final String COMMISSION_TYPE = "commissionType";



    private List<RefundLoaderError> refundLoaderErrors;

    public RefundDocumentValidator(List<RefundLoaderError> refundLoaderErrors) {
        this.refundLoaderErrors = refundLoaderErrors;
    }



    /**
     * Validates RefundDocument
     *  and adds the errors (if there is any) to RefundLoaderError collection.
     */
    public boolean isValid(RefundDocument refundDocument) {

        boolean result = isValidTransactionCode(refundDocument.getRecordIt02());

        if (!isValidIt05(refundDocument.getRecordsIt05())) {
            result = false;
        }
        if (!isValidCurrency(refundDocument)) {
            result = false;
        }
        if (!isValidTransactionNumbering(refundDocument)) {
            result = false;
        }
        if (!isValidRecordOrden(refundDocument)) {
            result = false;
        }
        return result;
    }



    private boolean isValidRecordOrden(RefundDocument refundDocument) {

        RecordIt02 it02 = refundDocument.getRecordIt02();

        if (it02 == null) {
            return true;
        }

        boolean result = true;

        int firstLineNumber = it02.getLineNumber();

        List<TransactionRecord> records = new ArrayList<>();
        records.addAll(refundDocument.getRecordsIt03());
        records.addAll(refundDocument.getRecordsIt05());
        records.addAll(refundDocument.getRecordsIt08());
        records.addAll(refundDocument.getRecordsIt0y());
        records.addAll(refundDocument.getRecordsIt0h());

        for (TransactionRecord record: records) {

            if (++firstLineNumber != record.getLineNumber()) {

                addToErrors(record, "recordIdentifier", INCORRECT_ORDEN);
                result = false;
            }
        }

        return result;
    }



    private boolean isValidCurrency(RefundDocument refundDocument) {

        List<RecordIt05> it05s = refundDocument.getRecordsIt05();

        boolean result = true;

        String cutp;
        if (it05s == null || it05s.isEmpty()) {
            cutp = null;
        } else {
            cutp = it05s.get(0).getCurrencyType();
            if (!isValidIt05Currency(it05s, cutp)) {
                result = false;
            }
        }

        List<RecordIt08> it08s = refundDocument.getRecordsIt08();
        if (it08s != null && !isValidIt08Currency(it08s, cutp)) {
            result = false;
        }

        return result;
    }


    private boolean isValidIt05Currency(List<RecordIt05> it05s, String firstCutp) {

        boolean result = true;
        for (int i = 0; i < it05s.size(); i++) {
            RecordIt05 it05 = it05s.get(i);
            String cutp = it05.getCurrencyType();
            if (StringUtils.isBlank(cutp)) {
                addToErrors(it05, CURRENCY_TYPE, MANDATORY);
                result = false;
            } else {
                if (!StringUtils.isBlank(firstCutp) && !firstCutp.equals(cutp)) {
                    addToErrors(it05, CURRENCY_TYPE, INCORRECT_CURRENCY);
                    result = false;
                }
            }
        }
        return result;
    }


    private boolean isValidIt08Currency(List<RecordIt08> it08s, String it05Cutp) {

        boolean result = true;
        for (int i = 0; i < it08s.size(); i++) {
            RecordIt08 it08 = it08s.get(i);
            String cutp = it08.getCurrencyType1();
            if (StringUtils.isBlank(cutp)) {
                addToErrors(it08, CURRENCY_TYPE + 1, MANDATORY);
                result = false;
            } else {
                if (!StringUtils.isBlank(it05Cutp) && !it05Cutp.equals(cutp)) {
                    addToErrors(it08, CURRENCY_TYPE + 1, INCORRECT_CURRENCY);
                    result = false;
                }
            }

            cutp = it08.getCurrencyType2();
            if (StringUtils.isBlank(cutp) && !isZero(it08.getFormOfPaymentAmount2())) {
                addToErrors(it08, CURRENCY_TYPE + 2, MANDATORY);
                result = false;
            }
            if (!StringUtils.isBlank(it05Cutp) && !StringUtils.isBlank(cutp)
                    && !it05Cutp.equals(cutp)) {
                addToErrors(it08, CURRENCY_TYPE + 2, INCORRECT_CURRENCY);
                result = false;
            }
        }
        return result;
    }


    private boolean isValidIt05(List<RecordIt05> it05s) {

        if (it05s == null || it05s.isEmpty()) {
            return true;
        }

        boolean result = isValidXlp(it05s.get(0));

        for (int i = 1; i < it05s.size(); i++) {
            RecordIt05 it05 = it05s.get(i);
            result = isValidZeroCommissionAmount(it05) && result;
            result = isValidZeroCommissionRate(it05) && result;
            result = isValidBlankCommissionType(it05) && result;
            if (!isZero(it05.getTicketDocumentAmount())) {
                addToErrors(it05, "ticketDocumentAmount", COMMISSION_TDAM_ON_FIRST_IT05);
                result = false;
            }
        }
        return result;
    }


    private boolean isValidXlp(RecordIt05 it05) {

        boolean result = true;
        if (StringUtils.isNotBlank(it05.getCommissionType1())) {
            addToErrors(it05, COMMISSION_TYPE + 1, FIRST_COMMISSION_TYPE_BLANK);
            result = false;
        }
        if ("XLP".equals(it05.getCommissionType2()) && "XLP".equals(it05.getCommissionType3())) {
            addToErrors(it05, COMMISSION_TYPE + 3, XLP_ONLY_ONCE);
            result = false;
        }
        if (StringUtils.isNotBlank(it05.getCommissionType2())
                && !"XLP".equals(it05.getCommissionType2())) {
            addToErrors(it05, COMMISSION_TYPE + 2, INVALID_COMMISSION_TYPE);
            result = false;
        }
        if (StringUtils.isNotBlank(it05.getCommissionType3())
                && !"XLP".equals(it05.getCommissionType3())) {
            addToErrors(it05, COMMISSION_TYPE + 3, INVALID_COMMISSION_TYPE);
            result = false;
        }
        return result;
    }


    private boolean isValidBlankCommissionType(RecordIt05 it05) {

        boolean result = true;
        if (StringUtils.isNotBlank(it05.getCommissionType1())) {
            addToErrors(it05, COMMISSION_TYPE + 1, FIRST_COMMISSION_TYPE_BLANK);
            result = false;
        }
        if (StringUtils.isNotBlank(it05.getCommissionType2())) {
            addToErrors(it05, COMMISSION_TYPE + 2, COMMISSION_TYPE_ON_FIRST_IT05);
            result = false;
        }
        if (StringUtils.isNotBlank(it05.getCommissionType3())) {
            addToErrors(it05, COMMISSION_TYPE + 3, COMMISSION_TYPE_ON_FIRST_IT05);
            result = false;
        }
        return result;
    }


    private boolean isValidZeroCommissionRate(RecordIt05 it05) {

        boolean result = true;
        if (!isZero(it05.getCommissionRate1())) {
            addToErrors(it05, "commissionRate1", COMMISSION_RATE_ON_FIRST_IT05);
            result = false;
        }
        if (!isZero(it05.getCommissionRate2())) {
            addToErrors(it05, "commissionRate2", COMMISSION_RATE_ON_FIRST_IT05);
            result = false;
        }
        if (!isZero(it05.getCommissionRate3())) {
            addToErrors(it05, "commissionRate3", COMMISSION_RATE_ON_FIRST_IT05);
            result = false;
        }
        return result;
    }


    private boolean isValidZeroCommissionAmount(RecordIt05 it05) {

        boolean result = true;
        if (!isZero(it05.getCommissionAmount1())) {
            addToErrors(it05, "commissionAmount1", COMMISSION_AMOUNT_ON_FIRST_IT05);
            result = false;
        }
        if (!isZero(it05.getCommissionAmount2())) {
            addToErrors(it05, "commissionAmount2", COMMISSION_AMOUNT_ON_FIRST_IT05);
            result = false;
        }
        if (!isZero(it05.getCommissionAmount3())) {
            addToErrors(it05, "commissionAmount3", COMMISSION_AMOUNT_ON_FIRST_IT05);
            result = false;
        }
        return result;
    }


    private boolean isZero(String value) {

        return StringUtils.isNotBlank(value) && value.matches("^0+");
    }


    private boolean isValidTransactionCode(RecordIt02 it02) {

        if (it02 != null && !"RFND".equals(it02.getTransactionCode())) {
            addToErrors(it02, "transactionCode", INCORRECT_TRANSACTION_CODE);
            return false;
        }
        return true;
    }



    private boolean isValidTransactionNumbering(RefundDocument refundDocument) {

        TransactionRecord it02 = refundDocument.getRecordIt02();
        if (it02 == null) {
            return true;
        }

        String trnn =  it02.getTransactionNumber();
        if (StringUtils.isBlank(trnn) || !trnn.matches("^\\d{6}$")) {

            addToErrors(it02, "transactionNumber", INCORRECT_TRANSACTION_NUMBER);
            return false;
        }

        List<TransactionRecord> records = new ArrayList<>();
        records.addAll(refundDocument.getRecordsIt03());
        records.addAll(refundDocument.getRecordsIt05());
        records.addAll(refundDocument.getRecordsIt08());
        records.addAll(refundDocument.getRecordsIt0h());
        records.addAll(refundDocument.getRecordsIt0y());

        boolean result = true;
        for (TransactionRecord record : records) {
            if (!isValidTransactionNumbering(record, trnn)) {
                result = false;
            }
        }

        return result;
    }


    private boolean isValidTransactionNumbering(TransactionRecord record,
            String transactionNumber) {

        if (transactionNumber.equals(record.getTransactionNumber())) {
            return true;
        }
        addToErrors(record, "transactionNumber", INCORRECT_TRANSACTION_NUMBER)
            .setTransactionNumber(transactionNumber);
        return false;
    }


    private RefundLoaderError addToErrors(TransactionRecord record, String field, String message) {

        RefundLoaderError error = new RefundLoaderError();
        error.setLineNumber(record.getLineNumber());
        error.setField(field);
        error.setTransactionNumber(record.getTransactionNumber());
        error.setRecordIdentifier(record.getRecordIdentifier());
        error.setMessage(message);
        refundLoaderErrors.add(error);
        return error;
    }
}
