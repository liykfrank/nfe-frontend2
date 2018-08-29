package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

import org.springframework.batch.item.file.transform.Range;

public interface RecordLayout {

    String getPattern();

    FieldLayout getFieldLayout(String field);

    List<FieldLayout> getFieldsLayouts();

    String[] getFieldsNames();

    Range[] getFieldsRanges();

    RecordIdentifier getRecordIdentifier();

}
