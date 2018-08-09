package org.iata.bsplink.refund.loader.validation;

import static org.apache.commons.lang.StringUtils.isAlpha;
import static org.apache.commons.lang.StringUtils.isAlphanumeric;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.iata.bsplink.refund.loader.utils.BeanPropertyUtils.getProperty;

import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.FieldType;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;
import org.iata.bsplink.refund.loader.model.record.TransactionRecord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Validates the content type (numeric, alphanumeric, ...) of each field.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecordFieldContentTypeValidator {

    private static final String FIELD_WITH_INVALID_CHARACTERS_MESSAGE = "Invalid character";
    private static final String FIELD_MUST_BE_NUMERIC_MESSAGE =
            "Non-numeric characters in numeric fields";

    private boolean result = true;
    private List<RefundLoaderError> refundLoaderErrors;

    /**
     * Validates the record.
     */
    public boolean validate(Record record, List<RefundLoaderError> refundLoaderErrors) {

        initializeValidator(refundLoaderErrors);

        validate(record);

        return result;
    }

    private void validate(Record record) {

        RecordLayout recordLayout = record.getRecordIdentifier().getLayout();

        for (String fieldName : recordLayout.getFieldsNames()) {

            FieldLayout fieldLayout = recordLayout.getFieldLayout(fieldName);
            String fieldValue = getFieldValue(record, fieldName);

            if (hasInvalidCharacters(fieldLayout, fieldValue)) {

                addError(record, fieldLayout, fieldValue, FIELD_WITH_INVALID_CHARACTERS_MESSAGE);

            } else if (isNumericWithWrongValue(fieldLayout, fieldValue)) {

                addError(record, fieldLayout, fieldValue, FIELD_MUST_BE_NUMERIC_MESSAGE);
            }
        }
    }

    private String getFieldValue(Object record, String fieldName) {

        try {

            return (String) getProperty(record, fieldName);

        } catch (Exception exception) {

            throw new RefundLoaderException(exception);
        }
    }

    private boolean hasInvalidCharacters(FieldLayout fieldLayout, String fieldValue) {

        return (FieldType.A.equals(fieldLayout.getType()) && !isAlpha(fieldValue))
                || (FieldType.AN.equals(fieldLayout.getType()) && !isAlphanumeric(fieldValue));
    }

    private boolean isNumericWithWrongValue(FieldLayout fieldLayout, String fieldValue) {

        return FieldType.N.equals(fieldLayout.getType()) && !isNumeric(fieldValue);
    }

    private void addError(Record record, FieldLayout layout, String fieldValue, String message) {

        RefundLoaderError error = new RefundLoaderError();

        error.setField(layout.getField());
        error.setFieldLayout(layout);
        error.setMessage(message);
        error.setDescription(String.format("Wrong field value \"%s\"", fieldValue));
        error.setRecordIdentifier(record.getRecordIdentifier());
        error.setLineNumber(record.getLineNumber());
        error.setValidationPhase(ValidationPhase.TRANSACTION);

        if (isTransactionRecord(record)) {

            error.setTransactionNumber(((TransactionRecord)record).getTransactionNumber());
        }

        refundLoaderErrors.add(error);
        result = false;
    }

    private boolean isTransactionRecord(Record record) {

        return record instanceof TransactionRecord;
    }

    private void initializeValidator(List<RefundLoaderError> refundLoaderErrors) {

        this.result = true;
        this.refundLoaderErrors = refundLoaderErrors;
    }

}
