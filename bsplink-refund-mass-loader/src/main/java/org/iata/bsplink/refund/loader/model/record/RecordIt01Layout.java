package org.iata.bsplink.refund.loader.model.record;

import java.util.HashMap;
import java.util.Map;

public class RecordIt01Layout implements RecordLayout {

    private static final String PATTERN = "1*";
    private Map<String, String> fieldsLayout = new HashMap<>();

    private RecordIt01Layout() {

        setFieldsLayout();
    }

    private void setFieldsLayout() {

        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("reportingSystemIdentifier", "8-11");
    }

    @Override
    public String getPattern() {

        return PATTERN;
    }

    @Override
    public Map<String, String> getFieldsLayout() {

        return fieldsLayout;
    }

    public static RecordIt01Layout getRecordLayout() {

        return new RecordIt01Layout();
    }

}
