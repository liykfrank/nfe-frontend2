package org.iata.bsplink.refund.loader.model.record;

public class RecordIt03Layout extends RecordBaseLayout {

    private static final String PATTERN = "3*";

    public RecordIt03Layout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT03;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("checkDigit1", 5, "CDGT", FieldType.N, 27, 1);
        addFieldLayout("checkDigit2", 8, "CDGT", FieldType.N, 47, 1);
        addFieldLayout("checkDigit3", 11, "CDGT", FieldType.N, 67, 1);
        addFieldLayout("checkDigit4", 14, "CDGT", FieldType.N, 87, 1);
        addFieldLayout("checkDigit5", 17, "CDGT", FieldType.N, 107, 1);
        addFieldLayout("checkDigit6", 20, "CDGT", FieldType.N, 127, 1);
        addFieldLayout("checkDigit7", 23, "CDGT", FieldType.N, 147, 1);
        addFieldLayout("dateOfIssueRelatedDocument", 24, "DIRD", FieldType.N, 148, 6);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier1", 3, "RCPN", FieldType.N, 8,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier2", 6, "RCPN", FieldType.N, 28,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier3", 9, "RCPN", FieldType.N, 48,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier4", 12, "RCPN", FieldType.N, 68,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier5", 15, "RCPN", FieldType.N, 88,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier6", 18, "RCPN", FieldType.N, 108,
                4);
        addFieldLayout("relatedTicketDocumentCouponNumberIdentifier7", 21, "RCPN", FieldType.N, 128,
                4);
        addFieldLayout("relatedTicketDocumentNumber1", 4, "RTDN", FieldType.AN, 12, 15);
        addFieldLayout("relatedTicketDocumentNumber2", 7, "RTDN", FieldType.AN, 32, 15);
        addFieldLayout("relatedTicketDocumentNumber3", 10, "RTDN", FieldType.AN, 52, 15);
        addFieldLayout("relatedTicketDocumentNumber4", 13, "RTDN", FieldType.AN, 72, 15);
        addFieldLayout("relatedTicketDocumentNumber5", 16, "RTDN", FieldType.AN, 92, 15);
        addFieldLayout("relatedTicketDocumentNumber6", 19, "RTDN", FieldType.AN, 112, 15);
        addFieldLayout("relatedTicketDocumentNumber7", 22, "RTDN", FieldType.AN, 132, 15);
        addFieldLayout("tourCode", 25, "TOUR", FieldType.AN, 154, 15);
        addFieldLayout("transactionNumber", 2, "TRNN", FieldType.N, 2, 6);
        addFieldLayout("waiverCode", 26, "WAVR", FieldType.AN, 169, 14);
    }

}
