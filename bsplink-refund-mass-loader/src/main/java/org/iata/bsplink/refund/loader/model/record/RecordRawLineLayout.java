package org.iata.bsplink.refund.loader.model.record;

import java.util.HashMap;
import java.util.Map;

/**
 * This layout describes a complete record without tokenization.
 */
public class RecordRawLineLayout implements RecordLayout {

    private static final String PATTERN = "*";
    private Map<String, String> fieldsLayout = new HashMap<>();

    private RecordRawLineLayout() {

        setFieldsLayout();
    }

    private void setFieldsLayout() {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("line", "2-253");
    }

    @Override
    public String getPattern() {

        return PATTERN;
    }

    @Override
    public Map<String, String> getFieldsLayout() {

        return fieldsLayout;
    }

    public static RecordRawLineLayout getRecordLayout() {

        return new RecordRawLineLayout();
    }

}
