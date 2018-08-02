package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt08Layout extends RecordBaseLayout {

    private static final String PATTERN = "8*";

    public RecordIt08Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "addressVerificationCode1", 12, "AVCD", FieldType.AN, 92, 2));
        fieldsLayouts.add(new FieldLayout(
                "addressVerificationCode2", 0, "AVCD", FieldType.AN, 215, 2));
        fieldsLayouts.add(new FieldLayout(
                "approvalCode1", 5, "APLC", FieldType.AN, 38, 6));
        fieldsLayouts.add(new FieldLayout(
                "approvalCode2", 0, "APLC", FieldType.AN, 161, 6));
        fieldsLayouts.add(new FieldLayout(
                "authorisedAmount1", 15, "AUTA", FieldType.AN, 120, 11));
        fieldsLayouts.add(new FieldLayout(
                "authorisedAmount2", 28, "AUTA", FieldType.AN, 243, 11));
        fieldsLayouts.add(new FieldLayout(
                "creditCardCorporateContract1", 11, "CRCC", FieldType.AN, 91, 1));
        fieldsLayouts.add(new FieldLayout(
                "creditCardCorporateContract2", 0, "CRCC", FieldType.AN, 214, 1));
        fieldsLayouts.add(new FieldLayout(
                "currencyType1", 6, "CUTP", FieldType.AN, 44, 4));
        fieldsLayouts.add(new FieldLayout(
                "currencyType2", 0, "CUTP", FieldType.AN, 167, 4));
        fieldsLayouts.add(new FieldLayout(
                "customerFileReference1", 10, "CSTF", FieldType.AN, 64, 27));
        fieldsLayouts.add(new FieldLayout(
                "customerFileReference2", 0, "CSTF", FieldType.AN, 187, 27));
        fieldsLayouts.add(new FieldLayout(
                "expiryDate1", 9, "EXDA", FieldType.AN, 60, 4));
        fieldsLayouts.add(new FieldLayout(
                "expiryDate2", 0, "EXDA", FieldType.AN, 183, 4));
        fieldsLayouts.add(new FieldLayout(
                "extendedPaymentCode1", 7, "EXPC", FieldType.AN, 48, 2));
        fieldsLayouts.add(new FieldLayout(
                "extendedPaymentCode2", 0, "EXPC", FieldType.AN, 171, 2));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentAccountNumber1", 3, "FPAC", FieldType.AN, 8, 19));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentAccountNumber2", 16, "FPAC", FieldType.AN, 131, 19));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentAmount1", 4, "FPAM", FieldType.N, 27, 11));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentAmount2", 17, "FPAM", FieldType.N, 150, 11));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentType1", 8, "FPTP", FieldType.AN, 50, 10));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentType2", 21, "FPTP", FieldType.AN, 173, 10));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentTransactionIdentifier1", 14, "FPTI", FieldType.AN, 95, 25));
        fieldsLayouts.add(new FieldLayout(
                "formOfPaymentTransactionIdentifier2", 0, "FPTI", FieldType.AN, 218, 25));
        fieldsLayouts.add(new FieldLayout(
                "sourceOfApprovalCode1", 13, "SAPP", FieldType.AN, 94, 1));
        fieldsLayouts.add(new FieldLayout(
                "sourceOfApprovalCode2", 0, "SAPP", FieldType.AN, 217, 1));
        fieldsLayouts.add(new FieldLayout(
                "transactionNumber", 2, "TRNN", FieldType.AN, 2, 6));
    }

}
