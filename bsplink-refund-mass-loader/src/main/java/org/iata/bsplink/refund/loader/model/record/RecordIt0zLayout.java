package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt0zLayout extends RecordBaseLayout {

    private static final String PATTERN = "Z*";

    public RecordIt0zLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout("reportRecordCounter", 10, "RRDC", FieldType.N, 2, 11));
    }

}
