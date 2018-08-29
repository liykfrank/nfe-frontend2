package org.iata.bsplink.refund.loader.model.record;

public class RecordIt0yLayout extends RecordBaseLayout {

    private static final String PATTERN = "Y*";

    public RecordIt0yLayout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT0Y;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("transactionNumber", 10, "TRNN", FieldType.N, 2, 6);
    }

}
