package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt08Layout extends RecordBaseLayout {

    private static final String PATTERN = "8*";

    public RecordIt08Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
    }

}
