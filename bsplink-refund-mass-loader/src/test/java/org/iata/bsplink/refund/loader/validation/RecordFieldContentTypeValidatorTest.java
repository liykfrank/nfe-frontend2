package org.iata.bsplink.refund.loader.validation;

import static org.apache.commons.beanutils.PropertyUtils.setProperty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
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

    private static final String FILE_MUST_BE_ALPHA_MESSAGE =
            "Non-alphabetic characters in alphabetic fields";

    private static final String FILE_MUST_BE_ALPHANUMERIC_MESSAGE =
            "Non-alphanumeric characters in alphanumeric fields";

    private static final String FILE_MUST_BE_NUMERIC_MESSAGE =
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
    public void testValidatesRecordFieldType(String fieldName, Record record, RecordLayout layout)
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

        if (shouldConsiderTransactionNumber(fieldName, record)) {
            assertThat(error.getTransactionNumber(), equalTo(TRANSACTION_NUMBER_1));
        }
    }

    private void initializeRecordAndAddWrongValue(Record record, RecordLayout layout,
            String fieldName) throws Exception {

        for (String layoutFieldName : layout.getFieldsNames()) {

            setProperty(record, layoutFieldName, "");
        }

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

    @SuppressWarnings({ "unused", "unchecked" })
    private Object[][] parametersForTestValidatesRecordFieldType() throws Exception {

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

                values.add(new Object[] { fieldName, recordType.newInstance(), layout });
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
                return FILE_MUST_BE_ALPHA_MESSAGE;

            case N:
                return FILE_MUST_BE_NUMERIC_MESSAGE;

            case AN:
                return FILE_MUST_BE_ALPHANUMERIC_MESSAGE;

            default:
                return null;
        }
    }

}
