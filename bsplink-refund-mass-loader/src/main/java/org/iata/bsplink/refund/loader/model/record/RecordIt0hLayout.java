package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt0hLayout extends RecordBaseLayout {

    private static final String PATTERN = "H*";

    public RecordIt0hLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
    }

}
