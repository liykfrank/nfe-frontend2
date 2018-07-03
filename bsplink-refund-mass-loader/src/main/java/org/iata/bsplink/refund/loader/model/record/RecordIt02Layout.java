package org.iata.bsplink.refund.loader.model.record;

import java.util.HashMap;
import java.util.Map;

public class RecordIt02Layout implements RecordLayout {

    private static final String PATTERN = "2*";
    private Map<String, String> fieldsLayout = new HashMap<>();

    private RecordIt02Layout() {

        setFieldsLayout();
    }

    private void setFieldsLayout() {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("transactionNumber", "2-7");
    }

    @Override
    public String getPattern() {

        return PATTERN;
    }

    @Override
    public Map<String, String> getFieldsLayout() {

        return fieldsLayout;
    }

    public static RecordIt02Layout getRecordLayout() {

        return new RecordIt02Layout();
    }

}
