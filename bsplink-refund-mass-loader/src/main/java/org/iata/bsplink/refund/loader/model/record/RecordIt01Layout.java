package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt01Layout extends RecordBaseLayout {

    private static final String PATTERN = "1*";

    public RecordIt01Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "fileType", 10, "FTYP", FieldType.AN, 37, 1));
        fieldsLayouts.add(new FieldLayout(
                "fileTypeSequenceNumber", 11, "FTSN", FieldType.AN, 38, 2));
        fieldsLayouts.add(new FieldLayout(
                "handbookRevisionNumber", 4, "REVN", FieldType.N, 12, 3));
        fieldsLayouts.add(new FieldLayout(
                "isoCountryCode", 8, "ISOC", FieldType.A, 29, 2));
        fieldsLayouts.add(new FieldLayout(
                "processingDate", 6, "PRDA", FieldType.N, 19, 6));
        fieldsLayouts.add(new FieldLayout(
                "processingTime", 7, "TIME", FieldType.N, 25, 4));
        fieldsLayouts.add(new FieldLayout(
                "reportingSystemIdentifier", 3, "RPSI", FieldType.AN, 8, 4));
        fieldsLayouts.add(new FieldLayout(
                "systemProviderReportingPeriodEndingDate", 2, "SPED", FieldType.N, 2, 6));
        fieldsLayouts.add(new FieldLayout(
                "testProductionStatus", 5, "TPST", FieldType.AN, 15, 4));
    }

}
