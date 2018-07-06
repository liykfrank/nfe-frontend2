package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt0yLayout extends RecordBaseLayout {

    private static final String PATTERN = "Y*";

    public RecordIt0yLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {
        // nothing to do
    }

}
