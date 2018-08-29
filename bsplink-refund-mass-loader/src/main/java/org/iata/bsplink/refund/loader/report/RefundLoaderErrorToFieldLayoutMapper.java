package org.iata.bsplink.refund.loader.report;

import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.ValidationErrorFieldLayouts;
import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordLayouts;
import org.springframework.stereotype.Component;

/**
 * Maps the error field to a field layout if it exist.
 */
@Component
public class RefundLoaderErrorToFieldLayoutMapper {

    /**
     * Adds the field layouts to the errors list.
     */
    public void addFieldLayouts(List<RefundLoaderError> errors) {

        errors.stream().forEach(x -> {

            RecordIdentifier recordIdentifier = x.getRecordIdentifier();

            if (recordIdentifier != null) {

                x.setFieldLayout(RecordLayouts.get(recordIdentifier).getFieldLayout(x.getField()));

            } else if (ValidationErrorFieldLayouts.exists(x.getField())) {

                FieldLayout fieldLayout = ValidationErrorFieldLayouts.get(x.getField());

                x.setFieldLayout(fieldLayout);
                x.setRecordIdentifier(fieldLayout.getRecordIdentifier());
            }

        });
    }

}
