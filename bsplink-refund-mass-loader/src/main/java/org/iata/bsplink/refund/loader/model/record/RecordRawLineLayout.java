package org.iata.bsplink.refund.loader.model.record;

/**
 * This layout describes a complete record without tokenization.
 */
public class RecordRawLineLayout extends RecordBaseLayout {

    private static final String PATTERN = "*";

    public RecordRawLineLayout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.UNKNOWN;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("line", 1, "", FieldType.AN, 1, 255);
    }

}
