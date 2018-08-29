package org.iata.bsplink.refund.loader.model.record;

public class RecordIt0zLayout extends RecordBaseLayout {

    private static final String PATTERN = "Z*";

    public RecordIt0zLayout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT0Z;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("reportRecordCounter", 10, "RRDC", FieldType.N, 2, 11);
    }

}
