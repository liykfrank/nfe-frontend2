package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt03Layout extends RecordBaseLayout {

    private static final String PATTERN = "3*";

    public RecordIt03Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "checkDigit1", 5, "CDGT", FieldType.N, 27, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit2", 8, "CDGT", FieldType.N, 47, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit3", 11, "CDGT", FieldType.N, 67, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit4", 14, "CDGT", FieldType.N, 87, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit5", 17, "CDGT", FieldType.N, 107, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit6", 20, "CDGT", FieldType.N, 127, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit7", 23, "CDGT", FieldType.N, 147, 1));
        fieldsLayouts.add(new FieldLayout(
                "dateOfIssueRelatedDocument", 24, "DIRD", FieldType.N, 148, 6));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier1", 3, "RCPN", FieldType.N, 8, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier2", 6, "RCPN", FieldType.N, 28, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier3", 9, "RCPN", FieldType.N, 48, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier4", 12, "RCPN", FieldType.N, 68, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier5", 15, "RCPN", FieldType.N, 88, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier6", 18, "RCPN", FieldType.N, 108, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentCouponNumberIdentifier7", 21, "RCPN", FieldType.N, 128, 4));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber1", 4, "RTDN", FieldType.AN, 12, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber2", 7, "RTDN", FieldType.AN, 32, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber3", 10, "RTDN", FieldType.AN, 52, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber4", 13, "RTDN", FieldType.AN, 72, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber5", 16, "RTDN", FieldType.AN, 92, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber6", 19, "RTDN", FieldType.AN, 112, 15));
        fieldsLayouts.add(new FieldLayout(
                "relatedTicketDocumentNumber7", 22, "RTDN", FieldType.AN, 132, 15));
        fieldsLayouts.add(new FieldLayout(
                "tourCode", 25, "TOUR", FieldType.AN, 154, 15));
        fieldsLayouts.add(new FieldLayout(
                "transactionNumber", 2, "TRNN", FieldType.N, 2, 6));
        fieldsLayouts.add(new FieldLayout(
                "waiverCode", 26, "WAVR", FieldType.AN, 169, 14));
    }

}
