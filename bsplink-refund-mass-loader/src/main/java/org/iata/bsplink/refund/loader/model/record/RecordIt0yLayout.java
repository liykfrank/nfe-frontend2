package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt0yLayout extends RecordBaseLayout {

    private static final String PATTERN = "Y*";

    public RecordIt0yLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout("transactionNumber", 10, "TRNN", FieldType.N, 2, 6));
    }

}
