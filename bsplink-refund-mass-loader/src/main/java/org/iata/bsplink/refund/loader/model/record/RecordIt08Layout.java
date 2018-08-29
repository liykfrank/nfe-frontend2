package org.iata.bsplink.refund.loader.model.record;

public class RecordIt08Layout extends RecordBaseLayout {

    private static final String PATTERN = "8*";

    public RecordIt08Layout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT08;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("addressVerificationCode1", 12, "AVCD", FieldType.AN, 92, 2);
        addFieldLayout("addressVerificationCode2", 0, "AVCD", FieldType.AN, 215, 2);
        addFieldLayout("approvalCode1", 5, "APLC", FieldType.AN, 38, 6);
        addFieldLayout("approvalCode2", 0, "APLC", FieldType.AN, 161, 6);
        addFieldLayout("authorisedAmount1", 15, "AUTA", FieldType.AN, 120, 11);
        addFieldLayout("authorisedAmount2", 28, "AUTA", FieldType.AN, 243, 11);
        addFieldLayout("creditCardCorporateContract1", 11, "CRCC", FieldType.AN, 91, 1);
        addFieldLayout("creditCardCorporateContract2", 0, "CRCC", FieldType.AN, 214, 1);
        addFieldLayout("currencyType1", 6, "CUTP", FieldType.AN, 44, 4);
        addFieldLayout("currencyType2", 0, "CUTP", FieldType.AN, 167, 4);
        addFieldLayout("customerFileReference1", 10, "CSTF", FieldType.AN, 64, 27);
        addFieldLayout("customerFileReference2", 0, "CSTF", FieldType.AN, 187, 27);
        addFieldLayout("expiryDate1", 9, "EXDA", FieldType.AN, 60, 4);
        addFieldLayout("expiryDate2", 0, "EXDA", FieldType.AN, 183, 4);
        addFieldLayout("extendedPaymentCode1", 7, "EXPC", FieldType.AN, 48, 2);
        addFieldLayout("extendedPaymentCode2", 0, "EXPC", FieldType.AN, 171, 2);
        addFieldLayout("formOfPaymentAccountNumber1", 3, "FPAC", FieldType.AN, 8, 19);
        addFieldLayout("formOfPaymentAccountNumber2", 16, "FPAC", FieldType.AN, 131, 19);
        addFieldLayout("formOfPaymentAmount1", 4, "FPAM", FieldType.N, 27, 11);
        addFieldLayout("formOfPaymentAmount2", 17, "FPAM", FieldType.N, 150, 11);
        addFieldLayout("formOfPaymentType1", 8, "FPTP", FieldType.AN, 50, 10);
        addFieldLayout("formOfPaymentType2", 21, "FPTP", FieldType.AN, 173, 10);
        addFieldLayout("formOfPaymentTransactionIdentifier1", 14, "FPTI", FieldType.AN, 95, 25);
        addFieldLayout("formOfPaymentTransactionIdentifier2", 0, "FPTI", FieldType.AN, 218, 25);
        addFieldLayout("sourceOfApprovalCode1", 13, "SAPP", FieldType.AN, 94, 1);
        addFieldLayout("sourceOfApprovalCode2", 0, "SAPP", FieldType.AN, 217, 1);
        addFieldLayout("transactionNumber", 2, "TRNN", FieldType.AN, 2, 6);
    }

}
