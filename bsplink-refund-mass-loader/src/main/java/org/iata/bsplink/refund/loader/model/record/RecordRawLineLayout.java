package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

/**
 * This layout describes a complete record without tokenization.
 */
public class RecordRawLineLayout extends RecordBaseLayout {

    private static final String PATTERN = "*";

    public RecordRawLineLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("line", "2-253");
    }

}
