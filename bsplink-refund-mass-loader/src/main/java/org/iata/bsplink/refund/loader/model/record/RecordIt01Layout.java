package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt01Layout extends RecordBaseLayout {

    private static final String PATTERN = "1*";

    public RecordIt01Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("reportingSystemIdentifier", "8-11");
    }

}
