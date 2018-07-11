package org.iata.bsplink.refund.loader.validation;

import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefundDocumentValidator {
    public static final String INCORRECT_TRANSACTION_CODE = "Incorrect transaction code";
    public static final String COMMISSION_AMOUNT_ON_FIRST_IT05 =
            "Commission Amount only has to be reported on the first IT05";

    @Autowired
    List<RefundLoaderError> refundLoaderErrors;

    /**
     * Validates RefundDocument
     *  and adds the errors (if there is any) to RefundLoaderError collection.
     */
    public boolean isValid(RefundDocument refundDocument) {
        boolean result = isValidTransactionCode(refundDocument.getRecordIt02());

        result = isValidCommission(refundDocument.getRecordsIt05()) && result;

        return result;
    }

    private boolean isValidCommission(List<RecordIt05> it05s) {
        if (it05s == null || it05s.size() < 2) {
            return true;
        }
        for (int i = 1; i < it05s.size(); i++) {
            RecordIt05 it05 = it05s.get(i);
            if (!isZero(it05.getCommissionAmount1())) {
                addToErrors(it05, "commissionAmount1", COMMISSION_AMOUNT_ON_FIRST_IT05);
            }
            if (!isZero(it05.getCommissionAmount2())) {
                addToErrors(it05, "commissionAmount2", COMMISSION_AMOUNT_ON_FIRST_IT05);
            }
            if (!isZero(it05.getCommissionAmount3())) {
                addToErrors(it05, "commissionAmount3", COMMISSION_AMOUNT_ON_FIRST_IT05);
            }
        }

        return false;
    }

    private boolean isZero(String value) {
        return value.matches("^0+");
    }

    private boolean isValidTransactionCode(RecordIt02 it02) {
        if (it02 != null && !"RFND".equals(it02.getTransactionCode())) {
            addToErrors(it02, "transactionCode", INCORRECT_TRANSACTION_CODE);
            return false;
        }
        return true;
    }

    private void addToErrors(TransactionRecord record, String field, String message) {
        RefundLoaderError error = new RefundLoaderError();
        error.setField(field);
        error.setTransactionNumber(record.getTransactionNumber());
        error.setRecordIdentifier(record.getRecordIdentifier());
        error.setMessage(message);
        refundLoaderErrors.add(error);
    }

}
