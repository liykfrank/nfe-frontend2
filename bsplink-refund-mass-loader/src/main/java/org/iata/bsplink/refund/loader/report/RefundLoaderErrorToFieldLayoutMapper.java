package org.iata.bsplink.refund.loader.report;

import java.util.List;
import java.util.Map;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.springframework.stereotype.Component;

/**
 * Maps the error field to a field layout if it exist.
 */
@Component
public class RefundLoaderErrorToFieldLayoutMapper {

    private Map<String, FieldLayout> fieldNameToFieldLayoutMap;

    /**
     * Creates the mapper.
     */
    public RefundLoaderErrorToFieldLayoutMapper(
            Map<String, FieldLayout> fieldNameToFieldLayoutMap) {

        this.fieldNameToFieldLayoutMap = fieldNameToFieldLayoutMap;
    }

    /**
     * Adds the field layouts to the errors list.
     */
    public void addFieldLayouts(List<RefundLoaderError> errors) {

        errors.stream().forEach(x -> {

            RecordIdentifier recordIdentifier = x.getRecordIdentifier();

            if (recordIdentifier != null) {

                x.setFieldLayout(recordIdentifier.getLayout().getFieldLayout(x.getField()));

            } else if (fieldNameToFieldLayoutMap.containsKey(x.getField())) {

                x.setFieldLayout(fieldNameToFieldLayoutMap.get(x.getField()));
            }

        });
    }

}
