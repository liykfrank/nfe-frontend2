package org.iata.bsplink.refund.loader.validation;

import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.getFileFixture;
import static org.junit.Assert.fail;

import org.iata.bsplink.refund.loader.exception.WrongFileFormatException;
import org.iata.bsplink.refund.loader.exception.WrongRecordCounterException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RefundFileValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testThrowsExceptionWhenRecordCounterIsWrong() throws Exception {

        thrown.expect(WrongRecordCounterException.class);
        thrown.expectMessage("wrong number of file lines: expected 14 there are 8");

        executeValidationOnFile("wrong/WRONG_RECORD_COUNTER");
    }

    private void executeValidationOnFile(String fileName) throws Exception {

        RefundFileValidator validator = new RefundFileValidator();

        validator.validate(getFileFixture(fileName).getFile());
    }

    @Test
    public void testValidationSucceedWhenFileIsCorrect() throws Exception {

        try {

            executeValidationOnFile("ALe9EARS_20170410_0744_016");

        } catch (Exception exception) {

            fail("Validation should be correct");
        }
    }

    @Test
    public void testThrowsExceptionIfThereIsNotFileTrailerRecord() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file does not have trailer record (IT0Z)");

        executeValidationOnFile("wrong/NO_FILE_TRAILER_RECORD");
    }

    @Test
    public void testThrowsExceptionIfFileIsEmpty() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file is empty");

        executeValidationOnFile("wrong/EMPTY_FILE");
    }

    @Test
    public void testThrowsExceptionIfRecordCountCanNotBeObtained() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the record count can not be extracted from file");

        executeValidationOnFile("wrong/WRONG_RECORD_COUNT_FORMAT");
    }

    @Test
    public void testThrowsExceptionIfThereIsNoHeaderRecord() throws Exception {

        thrown.expect(WrongFileFormatException.class);
        thrown.expectMessage("the file does not have header record (IT01)");

        executeValidationOnFile("wrong/NO_HEADER_RECORD");
    }

}
