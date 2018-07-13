package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.getFileFixture;
import static org.iata.bsplink.refund.loader.validation.RefundFileValidator.FILE_EMPTY;
import static org.iata.bsplink.refund.loader.validation.RefundFileValidator.INCORRECT_NUMBER_OF_RECORDS;
import static org.iata.bsplink.refund.loader.validation.RefundFileValidator.IT01_EXPECTED_IN_LINE1;
import static org.iata.bsplink.refund.loader.validation.RefundFileValidator.IT0Z_EXPECTED_IN_LASTLINE;
import static org.iata.bsplink.refund.loader.validation.RefundFileValidator.IT0Z_RRDC_INCORRECT_FORMAT;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.exception.WrongFileFormatException;
import org.iata.bsplink.refund.loader.exception.WrongRecordCounterException;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RefundFileValidatorTest {

    private List<RefundLoaderError> refundLoaderErrors;


    @Before
    public void setUp() throws Exception {

        refundLoaderErrors = new ArrayList<>();
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testThrowsExceptionWhenRecordCounterIsWrong() throws Exception {

        thrown.expect(WrongRecordCounterException.class);
        thrown.expectMessage("wrong number of file lines: expected 14 there are 8");

        RefundLoaderError error = it0zError(8, INCORRECT_NUMBER_OF_RECORDS);
        error.setField("reportRecordCounter");

        executeValidationOnFile("wrong/WRONG_RECORD_COUNTER", error);
    }

    private void executeValidationOnFile(String fileName,
            RefundLoaderError expectedRefundLoaderError) throws Exception {

        RefundFileValidator validator = new RefundFileValidator(refundLoaderErrors);

        try {

            validator.validate(getFileFixture(fileName).getFile());
        } catch (Exception e) {

            assertThat(refundLoaderErrors, hasSize(1));
            assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(expectedRefundLoaderError));

            throw e;
        }
    }

    @Test
    public void testValidationSucceedWhenFileIsCorrect() throws Exception {

        try {

            executeValidationOnFile("ALe9EARS_20170410_0744_016", null);

        } catch (Exception exception) {

            fail("Validation should be correct");
        }

        assertThat(refundLoaderErrors, IsEmptyCollection.empty());
    }


    @Test
    public void testThrowsExceptionIfThereIsNotFileTrailerRecord() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file does not have trailer record (IT0Z)");


        executeValidationOnFile("wrong/NO_FILE_TRAILER_RECORD",
                it0zError(7, IT0Z_EXPECTED_IN_LASTLINE));
    }

    @Test
    public void testThrowsExceptionIfFileIsEmpty() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file is empty");

        RefundLoaderError error = new RefundLoaderError();
        error.setMessage(FILE_EMPTY);
        executeValidationOnFile("wrong/EMPTY_FILE", error);
    }

    @Test
    public void testThrowsExceptionIfRecordCountCanNotBeObtained() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the record count can not be extracted from file");

        RefundLoaderError error = it0zError(8, IT0Z_RRDC_INCORRECT_FORMAT);
        error.setField("reportRecordCounter");

        executeValidationOnFile("wrong/WRONG_RECORD_COUNT_FORMAT", error);
    }

    @Test
    public void testThrowsExceptionIfThereIsNoHeaderRecord() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file does not have header record (IT01)");

        executeValidationOnFile("wrong/NO_HEADER_RECORD", it01Error(IT01_EXPECTED_IN_LINE1));
    }


    private RefundLoaderError it01Error(String message) {

        RefundLoaderError it01Error = new RefundLoaderError();
        it01Error.setRecordIdentifier(RecordIdentifier.IT01);
        it01Error.setField("recordIdentifier");
        it01Error.setLineNumber(1);
        it01Error.setMessage(message);
        return it01Error;
    }

    private RefundLoaderError it0zError(int lineNumber, String message) {

        RefundLoaderError it0zError = new RefundLoaderError();
        it0zError.setRecordIdentifier(RecordIdentifier.IT0Z);
        it0zError.setField("recordIdentifier");
        it0zError.setLineNumber(lineNumber);
        it0zError.setMessage(message);
        return it0zError;
    }
}
