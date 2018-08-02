package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt02Layout extends RecordBaseLayout {

    private static final String PATTERN = "2*";

    public RecordIt02Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "agentNumericCode", 3, "AGTN", FieldType.N, 8, 8));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationNumericCode1", 20, "ALNC", FieldType.N, 145, 8));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationNumericCode2", 24, "ALNC", FieldType.N, 174, 8));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationNumericCode3", 28, "ALNC", FieldType.N, 203, 8));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationType1", 17, "ALTP", FieldType.AN, 124, 1));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationType2", 21, "ALTP", FieldType.AN, 153, 1));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationType3", 25, "ALTP", FieldType.AN, 182, 1));
        fieldsLayouts.add(new FieldLayout(
                "approvedLocationType4", 29, "ALTP", FieldType.AN, 211, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit1", 9, "CDGT", FieldType.N, 47, 1));
        fieldsLayouts.add(new FieldLayout(
                "checkDigit2", 14, "CDGT", FieldType.N, 73, 1));
        fieldsLayouts.add(new FieldLayout(
                "conjunctionTicketIndicator", 4, "CJCP", FieldType.AN, 16, 3));
        fieldsLayouts.add(new FieldLayout(
                "couponUseIndicator", 5, "CPUI", FieldType.AN, 19, 4));
        fieldsLayouts.add(new FieldLayout(
                "dataInputStatusIndicator", 33, "DISI", FieldType.AN, 246, 1));
        fieldsLayouts.add(new FieldLayout(
                "dateOfIssue", 6, "DAIS", FieldType.N, 23, 6));
        fieldsLayouts.add(new FieldLayout(
                "formatIdentifier", 15, "FORM", FieldType.AN, 74, 1));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberFrom1", 22, "SCNF", FieldType.AN, 154, 16));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberFrom2", 26, "SCNF", FieldType.AN, 183, 16));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberFrom3", 30, "SCNF", FieldType.AN, 212, 16));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberTo1", 19, "SCNT", FieldType.N, 141, 4));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberTo2", 23, "SCNT", FieldType.N, 170, 4));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberTo3", 27, "SCNT", FieldType.N, 199, 4));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumberTo4", 31, "SCNT", FieldType.N, 228, 4));
        fieldsLayouts.add(new FieldLayout(
                "isoCountryCode", 34, "ISOC", FieldType.A, 247, 2));
        fieldsLayouts.add(new FieldLayout(
                "passengerName", 16, "PXNM", FieldType.AN, 75, 49));
        fieldsLayouts.add(new FieldLayout(
                "refundApplicationStatus", 37, "RFAS", FieldType.A, 255, 1));
        fieldsLayouts.add(new FieldLayout(
                "settlementAuthorisationCode", 32, "ESAC", FieldType.AN, 232, 14));
        fieldsLayouts.add(new FieldLayout(
                "statisticalCode", 7, "STAT", FieldType.AN, 29, 3));
        fieldsLayouts.add(new FieldLayout(
                "stockControlNumber", 18, "SCNF", FieldType.AN, 125, 16));
        fieldsLayouts.add(new FieldLayout(
                "ticketDocumentNumber", 8, "TDNR", FieldType.AN, 32, 15));
        fieldsLayouts.add(new FieldLayout(
                "ticketingAirlineCodeNumber", 13, "TACN", FieldType.AN, 68, 5));
        fieldsLayouts.add(new FieldLayout(
                "transactionCode", 10, "TRNC", FieldType.AN, 48, 4));
        fieldsLayouts.add(new FieldLayout(
                "transactionNumber", 2, "TRNN", FieldType.N, 2, 6));
        fieldsLayouts.add(new FieldLayout(
                "transmissionControlNumber", 11, "TCNR", FieldType.AN, 52, 15));
        fieldsLayouts.add(new FieldLayout(
                "transmissionControlNumberCheckDigit", 12, "TCND", FieldType.N, 67, 1));
        fieldsLayouts.add(new FieldLayout(
                "vendorIdentification", 36, "VNID", FieldType.AN, 251, 4));
        fieldsLayouts.add(new FieldLayout(
                "vendorIsoCountryCode", 35, "VISO", FieldType.A, 249, 2));
    }

}
