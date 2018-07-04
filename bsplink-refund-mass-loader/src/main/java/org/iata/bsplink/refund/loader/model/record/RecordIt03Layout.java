package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt03Layout extends RecordBaseLayout {

    private static final String PATTERN = "3*";

    public RecordIt03Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
    }

}
