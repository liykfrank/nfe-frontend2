package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt05Layout extends RecordBaseLayout {

    private static final String PATTERN = "5*";

    public RecordIt05Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "amountEnteredByAgent", 3, "AEBA", FieldType.N, 8, 11));
        fieldsLayouts.add(new FieldLayout(
                "amountPaidByCustomer", 31, "APBC", FieldType.N, 239, 11));
        fieldsLayouts.add(new FieldLayout(
                "commissionAmount1", 23, "COAM", FieldType.N, 182, 11));
        fieldsLayouts.add(new FieldLayout(
                "commissionAmount2", 26, "COAM", FieldType.N, 204, 11));
        fieldsLayouts.add(new FieldLayout(
                "commissionAmount3", 29, "COAM", FieldType.N, 226, 11));
        fieldsLayouts.add(new FieldLayout(
                "commissionRate1", 22, "CORT", FieldType.N, 177, 5));
        fieldsLayouts.add(new FieldLayout(
                "commissionRate2", 25, "CORT", FieldType.N, 199, 5));
        fieldsLayouts.add(new FieldLayout(
                "commissionRate3", 28, "CORT", FieldType.N, 221, 5));
        fieldsLayouts.add(new FieldLayout(
                "commissionType1", 21, "COTP", FieldType.AN, 171, 6));
        fieldsLayouts.add(new FieldLayout(
                "commissionType2", 24, "COTP", FieldType.AN, 193, 6));
        fieldsLayouts.add(new FieldLayout(
                "commissionType3", 27, "COTP", FieldType.AN, 215, 6));
        fieldsLayouts.add(new FieldLayout(
                "currencyType", 12, "CUTP", FieldType.AN, 98, 4));
        fieldsLayouts.add(new FieldLayout(
                "netReportingIndicator", 20, "NRID", FieldType.N, 237, 2));
        fieldsLayouts.add(new FieldLayout(
                "taxOnCommissionAmount", 13, "TOCA", FieldType.N, 102, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxOnCommissionType", 32, "TCTP", FieldType.AN, 250, 6));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount1", 6, "TMFA", FieldType.N, 38, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount2", 8, "TMFA", FieldType.N, 57, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount3", 10, "TMFA", FieldType.N, 76, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount4", 16, "TMFA", FieldType.N, 122, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount5", 18, "TMFA", FieldType.N, 141, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeAmount6", 20, "TMFA", FieldType.N, 160, 11));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType1", 5, "TMFT", FieldType.AN, 30, 8));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType2", 7, "TMFT", FieldType.AN, 49, 8));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType3", 9, "TMFT", FieldType.AN, 68, 8));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType4", 15, "TMFT", FieldType.AN, 114, 8));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType5", 17, "TMFT", FieldType.AN, 133, 8));
        fieldsLayouts.add(new FieldLayout(
                "taxMiscellaneousFeeType6", 19, "TMFT", FieldType.AN, 152, 8));
        fieldsLayouts.add(new FieldLayout(
                "ticketDocumentAmount", 11, "TDAM", FieldType.N, 87, 11));
        fieldsLayouts.add(new FieldLayout(
                "transactionNumber", 2, "TRNN", FieldType.N, 2, 6));

    }

}
