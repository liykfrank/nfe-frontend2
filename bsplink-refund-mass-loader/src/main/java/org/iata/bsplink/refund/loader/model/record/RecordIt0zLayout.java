package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt0zLayout extends RecordBaseLayout {

    private static final String PATTERN = "Z*";

    public RecordIt0zLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("reportRecordCounter", "2-12");
    }

}
