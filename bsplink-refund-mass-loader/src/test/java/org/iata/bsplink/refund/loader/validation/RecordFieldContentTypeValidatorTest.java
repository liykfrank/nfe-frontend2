package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.refund.loader.test.RecordUtils.initializeRecordFieldsWithEmptyStrings;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.iata.bsplink.refund.loader.utils.BeanPropertyUtils.getProperty;
import static org.iata.bsplink.refund.loader.utils.BeanPropertyUtils.setProperty;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.FieldType;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.iata.bsplink.refund.loader.model.record.RecordIt0z;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;
import org.iata.bsplink.refund.loader.model.record.TransactionRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RecordFieldContentTypeValidatorTest {

    private static final String FIELD_WITH_INVALID_CHARACTERS_MESSAGE = "Invalid character";
    private static final String FIELD_MUST_BE_NUMERIC_MESSAGE =
            "Non-numeric characters in numeric fields";

    private RecordFieldContentTypeValidator validator;
    private List<RefundLoaderError> errors;

    @Before
    public void setUp() {

        validator = new RecordFieldContentTypeValidator();
        errors = new ArrayList<>();
    }

    @Test
    @Parameters
    public void testValidatesRecordFieldTypeWithWrongValue(String fieldName, Record record,
            RecordLayout layout)
            throws Exception {

        initializeRecordAndAddWrongValue(record, layout, fieldName);

        assertThat(validator.validate(record, errors), equalTo(false));
        assertThat(errors, hasSize(1));

        RefundLoaderError error = errors.get(0);

        assertThat(error.getFieldLayout(), notNullValue());
        assertThat(error.getField(), equalTo(fieldName));
        assertThat(error.getRecordIdentifier(), equalTo(record.getRecordIdentifier()));
        assertThat(error.getValidationPhase(), equalTo(ValidationPhase.TRANSACTION));
        assertThat(error.getLineNumber(), equalTo(1));

        String errorMessage = getErrorMessageForType(layout.getFieldLayout(fieldName).getType());
        assertThat(error.getMessage(), equalTo(errorMessage));

        String fieldValue = (String) getProperty(record, fieldName);
        assertThat(error.getDescription(),
                equalTo(String.format("Wrong field value \"%s\"", fieldValue)));

        if (shouldConsiderTransactionNumber(fieldName, record)) {
            assertThat(error.getTransactionNumber(), equalTo(TRANSACTION_NUMBER_1));
        }
    }

    private void initializeRecordAndAddWrongValue(Record record, RecordLayout layout,
            String fieldName) throws Exception {

        initializeRecordFieldsWithEmptyStrings(record);

        FieldLayout fieldLayout = layout.getFieldLayout(fieldName);
        setProperty(record, fieldName, getIncorrectContentType(fieldLayout.getType()));

        record.setLineNumber(1);

        if (shouldConsiderTransactionNumber(fieldName, record)) {
            ((TransactionRecord)record).setTransactionNumber(TRANSACTION_NUMBER_1);
        }
    }

    private boolean shouldConsiderTransactionNumber(String fieldName, Record record) {

        return record instanceof TransactionRecord
                && fieldName != "transactionNumber";
    }

    @SuppressWarnings({ "unused" })
    private Object[][] parametersForTestValidatesRecordFieldTypeWithWrongValue() throws Exception {

        return getParametersForTestValidatesRecordFieldType(false);
    }

    @SuppressWarnings("unchecked")
    private Object[][] getParametersForTestValidatesRecordFieldType(boolean addAxFields)
            throws Exception {

        List<Object[]> values = new ArrayList<>();

        @SuppressWarnings("rawtypes")
        List<Class> recordTypes = Arrays.asList(
                RecordIt01.class,
                RecordIt02.class,
                RecordIt03.class,
                RecordIt05.class,
                RecordIt08.class,
                RecordIt0h.class,
                RecordIt0y.class,
                RecordIt0z.class);

        for (Class<Record> recordType : recordTypes) {

            RecordLayout layout = recordType.newInstance().getRecordIdentifier().getLayout();

            for (String fieldName : layout.getFieldsNames()) {

                if (!addAxFields
                        && FieldType.AX.equals(layout.getFieldLayout(fieldName).getType())) {
                    continue;
                }

                values.add(new Object[] {fieldName, recordType.newInstance(), layout});

            }
        }

        return values.toArray(new Object[][] {});
    }

    private String getIncorrectContentType(FieldType fieldType) {

        switch (fieldType) {

            case A:
                return "123";

            case N:
                return "ABC";

            case AN:
                return "...";

            default:
                return null;
        }
    }

    private String getErrorMessageForType(FieldType fieldType) {

        switch (fieldType) {

            case A:
                return FIELD_WITH_INVALID_CHARACTERS_MESSAGE;

            case N:
                return FIELD_MUST_BE_NUMERIC_MESSAGE;

            case AN:
                return FIELD_WITH_INVALID_CHARACTERS_MESSAGE;

            default:
                return null;
        }
    }

    @Test
    @Parameters
    public void testValidatesRecordFieldTypeWithRightValue(String fieldName, Record record,
            RecordLayout layout)
            throws Exception {

        initializeRecordAndAddRightValue(record, layout, fieldName);

        assertThat(validator.validate(record, errors), equalTo(true));
        assertThat(errors, hasSize(0));
    }

    @SuppressWarnings({ "unused" })
    private Object[][] parametersForTestValidatesRecordFieldTypeWithRightValue() throws Exception {

        return getParametersForTestValidatesRecordFieldType(true);
    }

    private void initializeRecordAndAddRightValue(Record record, RecordLayout layout,
            String fieldName) throws Exception {

        initializeRecordFieldsWithEmptyStrings(record);

        FieldLayout fieldLayout = layout.getFieldLayout(fieldName);
        setProperty(record, fieldName, getCorrectContentType(fieldLayout.getType()));
    }

    private String getCorrectContentType(FieldType fieldType) {

        switch (fieldType) {

            case A:
                return "ABC";

            case N:
                return "123";

            case AN:
                return "123ABC";

            case AX:
                return " @\\~\\EÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÖÜ£¥áíóúñÑªº¿¡ßØ±ãÃõÕÔÓ~€";

            default:
                return null;
        }
    }
}
