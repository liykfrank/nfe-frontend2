package org.iata.bsplink.refund.loader.model.record;

public class RecordIt02Layout extends RecordBaseLayout {

    private static final String PATTERN = "2*";

    public RecordIt02Layout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT02;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("agentNumericCode", 3, "AGTN", FieldType.N, 8, 8);
        addFieldLayout("approvedLocationNumericCode1", 20, "ALNC", FieldType.N, 145, 8);
        addFieldLayout("approvedLocationNumericCode2", 24, "ALNC", FieldType.N, 174, 8);
        addFieldLayout("approvedLocationNumericCode3", 28, "ALNC", FieldType.N, 203, 8);
        addFieldLayout("approvedLocationType1", 17, "ALTP", FieldType.AN, 124, 1);
        addFieldLayout("approvedLocationType2", 21, "ALTP", FieldType.AN, 153, 1);
        addFieldLayout("approvedLocationType3", 25, "ALTP", FieldType.AN, 182, 1);
        addFieldLayout("approvedLocationType4", 29, "ALTP", FieldType.AN, 211, 1);
        addFieldLayout("checkDigit1", 9, "CDGT", FieldType.N, 47, 1);
        addFieldLayout("checkDigit2", 14, "CDGT", FieldType.N, 73, 1);
        addFieldLayout("conjunctionTicketIndicator", 4, "CJCP", FieldType.AN, 16, 3);
        addFieldLayout("couponUseIndicator", 5, "CPUI", FieldType.AN, 19, 4);
        addFieldLayout("dataInputStatusIndicator", 33, "DISI", FieldType.AN, 246, 1);
        addFieldLayout("dateOfIssue", 6, "DAIS", FieldType.N, 23, 6);
        addFieldLayout("formatIdentifier", 15, "FORM", FieldType.AN, 74, 1);
        addFieldLayout("stockControlNumberFrom1", 22, "SCNF", FieldType.AN, 154, 16);
        addFieldLayout("stockControlNumberFrom2", 26, "SCNF", FieldType.AN, 183, 16);
        addFieldLayout("stockControlNumberFrom3", 30, "SCNF", FieldType.AN, 212, 16);
        addFieldLayout("stockControlNumberTo1", 19, "SCNT", FieldType.N, 141, 4);
        addFieldLayout("stockControlNumberTo2", 23, "SCNT", FieldType.N, 170, 4);
        addFieldLayout("stockControlNumberTo3", 27, "SCNT", FieldType.N, 199, 4);
        addFieldLayout("stockControlNumberTo4", 31, "SCNT", FieldType.N, 228, 4);
        addFieldLayout("isoCountryCode", 34, "ISOC", FieldType.A, 247, 2);
        addFieldLayout("passengerName", 16, "PXNM", FieldType.AN, 75, 49);
        addFieldLayout("refundApplicationStatus", 37, "RFAS", FieldType.A, 255, 1);
        addFieldLayout("settlementAuthorisationCode", 32, "ESAC", FieldType.AN, 232, 14);
        addFieldLayout("statisticalCode", 7, "STAT", FieldType.AN, 29, 3);
        addFieldLayout("stockControlNumber", 18, "SCNF", FieldType.AN, 125, 16);
        addFieldLayout("ticketDocumentNumber", 8, "TDNR", FieldType.AN, 32, 15);
        addFieldLayout("ticketingAirlineCodeNumber", 13, "TACN", FieldType.AN, 68, 5);
        addFieldLayout("transactionCode", 10, "TRNC", FieldType.AN, 48, 4);
        addFieldLayout("transactionNumber", 2, "TRNN", FieldType.N, 2, 6);
        addFieldLayout("transmissionControlNumber", 11, "TCNR", FieldType.AN, 52, 15);
        addFieldLayout("transmissionControlNumberCheckDigit", 12, "TCND", FieldType.N, 67, 1);
        addFieldLayout("vendorIdentification", 36, "VNID", FieldType.AN, 251, 4);
        addFieldLayout("vendorIsoCountryCode", 35, "VISO", FieldType.A, 249, 2);
    }

}
