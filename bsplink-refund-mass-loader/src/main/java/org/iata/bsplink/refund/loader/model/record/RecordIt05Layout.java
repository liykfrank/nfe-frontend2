package org.iata.bsplink.refund.loader.model.record;

public class RecordIt05Layout extends RecordBaseLayout {

    private static final String PATTERN = "5*";

    public RecordIt05Layout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT05;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("amountEnteredByAgent", 3, "AEBA", FieldType.N, 8, 11);
        addFieldLayout("amountPaidByCustomer", 31, "APBC", FieldType.N, 239, 11);
        addFieldLayout("commissionAmount1", 23, "COAM", FieldType.N, 182, 11);
        addFieldLayout("commissionAmount2", 26, "COAM", FieldType.N, 204, 11);
        addFieldLayout("commissionAmount3", 29, "COAM", FieldType.N, 226, 11);
        addFieldLayout("commissionRate1", 22, "CORT", FieldType.N, 177, 5);
        addFieldLayout("commissionRate2", 25, "CORT", FieldType.N, 199, 5);
        addFieldLayout("commissionRate3", 28, "CORT", FieldType.N, 221, 5);
        addFieldLayout("commissionType1", 21, "COTP", FieldType.AN, 171, 6);
        addFieldLayout("commissionType2", 24, "COTP", FieldType.AN, 193, 6);
        addFieldLayout("commissionType3", 27, "COTP", FieldType.AN, 215, 6);
        addFieldLayout("currencyType", 12, "CUTP", FieldType.AN, 98, 4);
        addFieldLayout("netReportingIndicator", 20, "NRID", FieldType.N, 237, 2);
        addFieldLayout("taxOnCommissionAmount", 13, "TOCA", FieldType.N, 102, 11);
        addFieldLayout("taxOnCommissionType", 32, "TCTP", FieldType.AN, 250, 6);
        addFieldLayout("taxMiscellaneousFeeAmount1", 6, "TMFA", FieldType.N, 38, 11);
        addFieldLayout("taxMiscellaneousFeeAmount2", 8, "TMFA", FieldType.N, 57, 11);
        addFieldLayout("taxMiscellaneousFeeAmount3", 10, "TMFA", FieldType.N, 76, 11);
        addFieldLayout("taxMiscellaneousFeeAmount4", 16, "TMFA", FieldType.N, 122, 11);
        addFieldLayout("taxMiscellaneousFeeAmount5", 18, "TMFA", FieldType.N, 141, 11);
        addFieldLayout("taxMiscellaneousFeeAmount6", 20, "TMFA", FieldType.N, 160, 11);
        addFieldLayout("taxMiscellaneousFeeType1", 5, "TMFT", FieldType.AN, 30, 8);
        addFieldLayout("taxMiscellaneousFeeType2", 7, "TMFT", FieldType.AN, 49, 8);
        addFieldLayout("taxMiscellaneousFeeType3", 9, "TMFT", FieldType.AN, 68, 8);
        addFieldLayout("taxMiscellaneousFeeType4", 15, "TMFT", FieldType.AN, 114, 8);
        addFieldLayout("taxMiscellaneousFeeType5", 17, "TMFT", FieldType.AN, 133, 8);
        addFieldLayout("taxMiscellaneousFeeType6", 19, "TMFT", FieldType.AN, 152, 8);
        addFieldLayout("ticketDocumentAmount", 11, "TDAM", FieldType.N, 87, 11);
        addFieldLayout("transactionNumber", 2, "TRNN", FieldType.N, 2, 6);
    }

}
