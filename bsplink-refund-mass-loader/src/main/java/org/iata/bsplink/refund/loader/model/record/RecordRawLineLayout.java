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

        fieldsLayout.put("line", "1-254");
    }

}
