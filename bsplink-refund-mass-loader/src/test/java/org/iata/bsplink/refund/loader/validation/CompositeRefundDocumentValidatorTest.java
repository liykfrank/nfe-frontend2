package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CompositeRefundDocumentValidatorTest {

    private CompositeRefundDocumentValidator validator;
    private RecordFieldContentTypeValidator recordValidator;
    private RefundDocumentValidator documentValidator;
    private RefundDocument refund;
    private List<RefundLoaderError> errors;

    @Before
    public void setUp() {

        recordValidator = mock(RecordFieldContentTypeValidator.class);
        documentValidator = mock(RefundDocumentValidator.class);
        validator = new CompositeRefundDocumentValidator(recordValidator, documentValidator);

        refund = new RefundDocument();
        errors = new ArrayList<>();
    }

    @Test
    public void testExecutesDocumentValidatorIfRecordValidationSuccess() {

        when(recordValidator.validate(any(), eq(errors))).thenReturn(true);

        validator.validate(refund, errors);

        verify(documentValidator).validate(refund, errors);
    }

    @Test
    public void testDoesNotExecuteDocumentValidatorIfRecordValidationFails() {

        when(recordValidator.validate(any(), eq(errors))).thenReturn(false);

        validator.validate(refund, errors);

        verify(documentValidator).validate(refund, errors);
    }

    @Test
    public void testValidationFailsIfRecordValidationFails() {

        when(recordValidator.validate(any(), eq(errors))).thenReturn(false);

        assertThat(validator.validate(refund, errors), equalTo(false));
    }

    @Test
    public void testValidationFailsIfDocumentValidationFails() {

        when(recordValidator.validate(any(), eq(errors))).thenReturn(true);
        when(documentValidator.validate(any(), eq(errors))).thenReturn(false);

        assertThat(validator.validate(refund, errors), equalTo(false));
    }

    @Test
    public void testValidationSuccess() {

        when(recordValidator.validate(any(), eq(errors))).thenReturn(true);
        when(documentValidator.validate(any(), eq(errors))).thenReturn(true);

        assertThat(validator.validate(refund, errors), equalTo(true));
    }

    @Test
    public void testRecordValidationIsAppliedToIt02Record() {

        when(documentValidator.validate(any(), eq(errors))).thenReturn(true);

        RecordIt02 recordIt02 = new RecordIt02();
        refund.setRecordIt02(recordIt02);

        validator.validate(refund, errors);

        verify(recordValidator, times(1)).validate(recordIt02, errors);
    }

    @Test
    public void testRecordValidationIsAppliedToIt03Records() {

        RecordIt03 firstRecord = new RecordIt03();
        RecordIt03 secondRecord = new RecordIt03();

        refund.addRecordIt03(firstRecord);
        refund.addRecordIt03(secondRecord);

        testRecordValidationIsAppliedToRecords(refund, firstRecord, secondRecord);
    }

    @Test
    public void testRecordValidationIsAppliedToIt05Records() {

        RecordIt05 firstRecord = new RecordIt05();
        RecordIt05 secondRecord = new RecordIt05();

        refund.addRecordIt05(firstRecord);
        refund.addRecordIt05(secondRecord);

        testRecordValidationIsAppliedToRecords(refund, firstRecord, secondRecord);
    }

    @Test
    public void testRecordValidationIsAppliedToIt08Records() {

        RecordIt08 firstRecord = new RecordIt08();
        RecordIt08 secondRecord = new RecordIt08();

        refund.addRecordIt08(firstRecord);
        refund.addRecordIt08(secondRecord);

        testRecordValidationIsAppliedToRecords(refund, firstRecord, secondRecord);
    }

    @Test
    public void testRecordValidationIsAppliedToIt0hRecords() {

        RecordIt0h firstRecord = new RecordIt0h();
        RecordIt0h secondRecord = new RecordIt0h();

        refund.addRecordIt0h(firstRecord);
        refund.addRecordIt0h(secondRecord);

        testRecordValidationIsAppliedToRecords(refund, firstRecord, secondRecord);
    }

    @Test
    public void testRecordValidationIsAppliedToIt0zRecords() {

        RecordIt0y firstRecord = new RecordIt0y();
        RecordIt0y secondRecord = new RecordIt0y();

        refund.addRecordIt0y(firstRecord);
        refund.addRecordIt0y(secondRecord);

        testRecordValidationIsAppliedToRecords(refund, firstRecord, secondRecord);
    }

    private void testRecordValidationIsAppliedToRecords(RefundDocument refund, Record firstRecord,
            Record secondRecord) {

        when(documentValidator.validate(any(), eq(errors))).thenReturn(true);

        validator.validate(refund, errors);

        ArgumentCaptor<Record> captor = ArgumentCaptor.forClass(Record.class);

        verify(recordValidator, times(2)).validate(captor.capture(), eq(errors));

        assertThat(captor.getAllValues().get(0), equalTo(firstRecord));
        assertThat(captor.getAllValues().get(1), equalTo(secondRecord));
    }
}
