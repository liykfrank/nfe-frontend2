package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt02Layout extends RecordBaseLayout {

    private static final String PATTERN = "2*";

    public RecordIt02Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("transactionNumber", "2-7");
    }

}
