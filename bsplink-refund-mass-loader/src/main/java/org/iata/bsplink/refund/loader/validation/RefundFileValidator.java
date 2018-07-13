package org.iata.bsplink.refund.loader.validation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.exception.WrongFileFormatException;
import org.iata.bsplink.refund.loader.exception.WrongRecordCounterException;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.springframework.stereotype.Component;

/**
 * Basic refund file validations.
 */
@Component
public class RefundFileValidator {

    public static final String FILE_EMPTY = "The file is empty";
    public static final String IT01_EXPECTED_IN_LINE1 =
            "The IT01 Record is expected to be reported in first line.";
    public static final String IT0Z_EXPECTED_IN_LASTLINE =
            "The IT0Z Record is expected to be reported in last line.";
    public static final String IT0Z_RRDC_INCORRECT_FORMAT = null;
    public static final String INCORRECT_NUMBER_OF_RECORDS =
            "Incorrect number of records reported in Report Record Counter field.";

    private List<RefundLoaderError> refundLoaderErrors;

    public RefundFileValidator(List<RefundLoaderError> refundLoaderErrors) {
        this.refundLoaderErrors = refundLoaderErrors;
    }


    /**
     * Does the file validation and throws an exception if there is an error.
     *
     * <p>
     * If a validation error occurs a exception is thrown.
     * </p>
     */
    public void validate(File file) {

        throwExeptionIfFileIsEmpty(file);

        String currentLine = null;
        Optional<String> optionalLastReadLine = Optional.empty();
        int actualLinesCount = 0;

        try (FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader)) {

            while ((currentLine = lineNumberReader.readLine()) != null) {

                optionalLastReadLine = Optional.ofNullable(currentLine);
                actualLinesCount++;

                if (actualLinesCount == 1) {

                    throwExceptionIfThereIsNoFileHeaderRecord(currentLine);
                }
            }

        } catch (IOException exception) {

            throw new RefundLoaderException(exception);
        }

        throwExceptionIfThereIsNoFileTrailerRecord(optionalLastReadLine);

        int expectedLinesCount = extractRecordCounter(optionalLastReadLine);

        if (expectedLinesCount != actualLinesCount) {

            addToErrors(RecordIdentifier.IT0Z, "reportRecordCounter", INCORRECT_NUMBER_OF_RECORDS);

            throw new WrongRecordCounterException(actualLinesCount, expectedLinesCount);
        }

    }

    private void throwExeptionIfFileIsEmpty(File file) {

        if (file.length() <= 0) {

            addToErrors(null, null, FILE_EMPTY);

            throw new WrongFileFormatException("the file is empty");
        }

    }

    private void throwExceptionIfThereIsNoFileHeaderRecord(String line) {

        if (!line.startsWith("1")) {

            addToErrors(RecordIdentifier.IT01, "recordIdentifier", IT01_EXPECTED_IN_LINE1);

            throw new WrongFileFormatException("the file does not have header record (IT01)");
        }
    }


    private void throwExceptionIfThereIsNoFileTrailerRecord(Optional<String> optionalLastReadLine) {

        if (!(optionalLastReadLine.isPresent() && optionalLastReadLine.get().startsWith("Z"))) {

            addToErrors(RecordIdentifier.IT0Z, "recordIdentifier", IT0Z_EXPECTED_IN_LASTLINE);

            throw new WrongFileFormatException("the file does not have trailer record (IT0Z)");
        }
    }

    private int extractRecordCounter(Optional<String> optionalLastReadLine) {

        if (optionalLastReadLine.isPresent()) {

            String recordCount = optionalLastReadLine.get().substring(2, 12);

            try {
                return Integer.parseInt(recordCount);
            } catch (NumberFormatException exception) {
                // do nothing
            }
        }

        addToErrors(RecordIdentifier.IT0Z, "reportRecordCounter", IT0Z_RRDC_INCORRECT_FORMAT);

        // if the record count was not returned yet there is a problem extracting the value
        throw new WrongFileFormatException("the record count can not be extracted from file");
    }


    private void addToErrors(RecordIdentifier recordIdentifier, String field, String message) {

        RefundLoaderError error = new RefundLoaderError();
        error.setField(field);
        error.setRecordIdentifier(recordIdentifier);
        error.setMessage(message);
        refundLoaderErrors.add(error);
    }
}
