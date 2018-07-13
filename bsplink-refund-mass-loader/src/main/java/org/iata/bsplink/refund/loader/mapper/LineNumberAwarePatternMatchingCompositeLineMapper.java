package org.iata.bsplink.refund.loader.mapper;

import org.iata.bsplink.refund.loader.model.record.Record;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;

public class LineNumberAwarePatternMatchingCompositeLineMapper
        extends PatternMatchingCompositeLineMapper<Record> {

    @Override
    public Record mapLine(String line, int lineNumber) throws Exception {

        Record record = super.mapLine(line, lineNumber);
        record.setLineNumber(lineNumber);

        return record;
    }

}
