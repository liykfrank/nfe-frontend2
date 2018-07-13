package org.iata.bsplink.refund.loader.validation;

import static org.hamcrest.CoreMatchers.equalTo;
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

        executeValidationOnFile("wrong/WRONG_RECORD_COUNTER", INCORRECT_NUMBER_OF_RECORDS);
    }

    private void executeValidationOnFile(String fileName, String refundLoaderErrorMessage)
            throws Exception {

        RefundFileValidator validator = new RefundFileValidator(refundLoaderErrors);

        try {

            validator.validate(getFileFixture(fileName).getFile());
        } catch (Exception e) {

            assertThat(refundLoaderErrors, hasSize(1));
            assertThat(refundLoaderErrors.get(0).getMessage(), equalTo(refundLoaderErrorMessage));

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

        executeValidationOnFile("wrong/NO_FILE_TRAILER_RECORD", IT0Z_EXPECTED_IN_LASTLINE);
    }

    @Test
    public void testThrowsExceptionIfFileIsEmpty() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file is empty");

        executeValidationOnFile("wrong/EMPTY_FILE", FILE_EMPTY);
    }

    @Test
    public void testThrowsExceptionIfRecordCountCanNotBeObtained() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the record count can not be extracted from file");

        executeValidationOnFile("wrong/WRONG_RECORD_COUNT_FORMAT", IT0Z_RRDC_INCORRECT_FORMAT);
    }

    @Test
    public void testThrowsExceptionIfThereIsNoHeaderRecord() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file does not have header record (IT01)");

        executeValidationOnFile("wrong/NO_HEADER_RECORD", IT01_EXPECTED_IN_LINE1);
    }

}
