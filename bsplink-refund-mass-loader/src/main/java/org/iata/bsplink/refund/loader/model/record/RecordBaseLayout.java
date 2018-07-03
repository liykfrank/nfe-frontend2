package org.iata.bsplink.refund.loader.model.record;

import java.util.HashMap;
import java.util.Map;

public abstract class RecordBaseLayout implements RecordLayout {

    private String recordPattern;
    private Map<String, String> fieldsLayout = new HashMap<>();

    protected RecordBaseLayout(String recordPattern) {

        this.recordPattern = recordPattern;
        setFieldsLayout(fieldsLayout);
    }

    /**
     * Adds the fields layout of the record.
     */
    protected abstract void setFieldsLayout(Map<String, String> fieldsLayout);

    @Override
    public String getPattern() {

        return recordPattern;
    }

    @Override
    public Map<String, String> getFieldsLayout() {

        return fieldsLayout;
    }

}
