package org.iata.bsplink.refund.loader.model.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.file.transform.Range;

public abstract class RecordBaseLayout implements RecordLayout {

    private String recordPattern;
    private List<FieldLayout> fieldsLayout = new ArrayList<>();
    private Map<String, FieldLayout> layoutsByField = new HashMap<>();

    protected RecordBaseLayout(String recordPattern) {

        this.recordPattern = recordPattern;
        setFieldsLayouts(fieldsLayout);
        setLayoutsByField(fieldsLayout);
    }

    /**
     * Adds the fields layout of the record.
     */
    protected abstract void setFieldsLayouts(List<FieldLayout> fieldsLayout);

    private void setLayoutsByField(List<FieldLayout> fieldsLayout) {

        fieldsLayout.stream().forEach(x ->
            layoutsByField.put(x.getField(), x)
        );
    }

    @Override
    public String getPattern() {

        return recordPattern;
    }

    @Override
    public FieldLayout getFieldLayout(String field) {

        return layoutsByField.get(field);
    }

    @Override
    public List<FieldLayout> getFieldsLayouts() {

        return fieldsLayout;
    }

    /**
     * Returns an array with the field names.
     */
    @Override
    public String[] getFieldsNames() {

        return fieldsLayout.stream()
            .map(FieldLayout::getField)
            .collect(Collectors.toList())
            .toArray(new String[] {});
    }

    /**
     * Returns an array with the field ranges.
     */
    @Override
    public Range[] getFieldsRanges() {

        return fieldsLayout.stream()
            .map(FieldLayout::getRange)
            .collect(Collectors.toList())
            .toArray(new Range[] {});
    }

}
