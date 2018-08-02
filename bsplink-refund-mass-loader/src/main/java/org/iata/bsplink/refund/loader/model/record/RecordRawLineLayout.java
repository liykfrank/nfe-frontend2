package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

/**
 * This layout describes a complete record without tokenization.
 */
public class RecordRawLineLayout extends RecordBaseLayout {

    private static final String PATTERN = "*";

    public RecordRawLineLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout("line", 1, "", FieldType.AN, 1, 255));
    }

}
