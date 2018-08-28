package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_EMPTY_FILE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_NO_FILE_TRAILER_RECORD;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_NO_HEADER_RECORD;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_WITHOUT_EOL;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_WITH_MORE_THAN_127_RECORDS;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_WRONG_RECORD_COUNTER;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.FILE_WRONG_RECORD_COUNT_FORMAT;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.getFileFixture;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.junit.Before;
import org.junit.Test;

public class RefundFileValidatorTest {

    private static final String FILE_EMPTY = "The file is empty";
    private static final String IT01_EXPECTED_IN_LINE1 =
            "The IT01 Record is expected to be reported in first line.";
    private static final String IT0Z_EXPECTED_IN_LASTLINE =
            "The IT0Z Record is expected to be reported in last line.";
    private static final String IT0Z_RRDC_INCORRECT_FORMAT =
            "The report record counter doesn't have a valid format.";
    private static final String INCORRECT_NUMBER_OF_RECORDS =
            "Incorrect number of records reported in Report Record Counter field.";

    private RefundFileValidator validator;
    private List<RefundLoaderError> refundLoaderErrors;

    @Before
    public void setUp() {

        refundLoaderErrors = new ArrayList<>();
        validator = new RefundFileValidator();
    }

    @Test
    public void testValidatesRecordCounter() {

        RefundLoaderError expectedError = it0zError(8, INCORRECT_NUMBER_OF_RECORDS);
        expectedError.setField("reportRecordCounter");

        executeValidationOnFile(FILE_WRONG_RECORD_COUNTER);

        assertError(expectedError);
    }

    /**
     * Bug NFEIATA-286 Error when e9 file has more than 99 records.
     *
     * <p>
     * There was an error in the comparison because was used the "==" operator instead of the
     * equals() method so with less than 127 records the comparison was correct but with more
     * records it failed.
     * </p>
     */
    @Test
    public void testValidatesRecordCounterWhenThereAreMoreThan127Records() {

        executeValidationOnFile(FILE_WITH_MORE_THAN_127_RECORDS);

        assertThat("The validation should have no errors: " + refundLoaderErrors.toString(),
                refundLoaderErrors, hasSize(0));
    }

    @Test
    public void testValidatesRecordCounterWhenFileDoesnHaveEndOfLine() {

        executeValidationOnFile(FILE_WITHOUT_EOL);

        assertThat("The validation should have no errors: " + refundLoaderErrors.toString(),
                refundLoaderErrors, hasSize(0));
    }

    private void executeValidationOnFile(String fileName) {

        try {

            validator.validate(getFileFixture(fileName).getFile(), refundLoaderErrors);

        } catch (Exception exception) {

            throw new RuntimeException(exception);
        }
    }

    private void assertError(RefundLoaderError expected) {

        assertThat(refundLoaderErrors, hasSize(1));
        assertThat(refundLoaderErrors.get(0), samePropertyValuesAs(expected));
    }

    @Test
    public void testValidationSucceedWhenFileIsCorrect() {

        executeValidationOnFile("ALe9EARS_20170410_0744_016");

        assertThat(refundLoaderErrors, IsEmptyCollection.empty());
    }

    @Test
    public void testValidatesFileTrailerRecord() {

        executeValidationOnFile(FILE_NO_FILE_TRAILER_RECORD);

        assertError(it0zError(7, IT0Z_EXPECTED_IN_LASTLINE));
    }

    @Test
    public void testValidatesFileIsEmpty() {

        RefundLoaderError expectedError = new RefundLoaderError();
        expectedError.setMessage(FILE_EMPTY);
        expectedError.setValidationPhase(ValidationPhase.FILE);

        executeValidationOnFile(FILE_EMPTY_FILE);

        assertError(expectedError);
    }

    @Test
    public void testIsAwareOfRecordCountExtractionErrors() {

        RefundLoaderError expectedError = it0zError(8, IT0Z_RRDC_INCORRECT_FORMAT);
        expectedError.setField("reportRecordCounter");

        executeValidationOnFile(FILE_WRONG_RECORD_COUNT_FORMAT);

        assertError(expectedError);
    }

    @Test
    public void testValidatesHeaderRecord() {

        executeValidationOnFile(FILE_NO_HEADER_RECORD);

        assertError(it01Error(IT01_EXPECTED_IN_LINE1));
    }


    private RefundLoaderError it01Error(String message) {

        RefundLoaderError it01Error = new RefundLoaderError();

        it01Error.setRecordIdentifier(RecordIdentifier.IT01);
        it01Error.setField("recordIdentifier");
        it01Error.setLineNumber(1);
        it01Error.setMessage(message);
        it01Error.setValidationPhase(ValidationPhase.FILE);

        return it01Error;
    }

    private RefundLoaderError it0zError(int lineNumber, String message) {

        RefundLoaderError it0zError = new RefundLoaderError();

        it0zError.setRecordIdentifier(RecordIdentifier.IT0Z);
        it0zError.setField("recordIdentifier");
        it0zError.setLineNumber(lineNumber);
        it0zError.setMessage(message);
        it0zError.setValidationPhase(ValidationPhase.FILE);

        return it0zError;
    }
}
