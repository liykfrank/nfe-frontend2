package org.iata.bsplink.refund.loader.model.record;

public class RecordIt01Layout extends RecordBaseLayout {

    private static final String PATTERN = "1*";

    public RecordIt01Layout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT01;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("fileType", 10, "FTYP", FieldType.AN, 37, 1);
        addFieldLayout("fileTypeSequenceNumber", 11, "FTSN", FieldType.AN, 38, 2);
        addFieldLayout("handbookRevisionNumber", 4, "REVN", FieldType.N, 12, 3);
        addFieldLayout("isoCountryCode", 8, "ISOC", FieldType.A, 29, 2);
        addFieldLayout("processingDate", 6, "PRDA", FieldType.N, 19, 6);
        addFieldLayout("processingTime", 7, "TIME", FieldType.N, 25, 4);
        addFieldLayout("reportingSystemIdentifier", 3, "RPSI", FieldType.AN, 8, 4);
        addFieldLayout("systemProviderReportingPeriodEndingDate", 2, "SPED", FieldType.N, 2, 6);
        addFieldLayout("testProductionStatus", 5, "TPST", FieldType.AN, 15, 4);
    }

}
