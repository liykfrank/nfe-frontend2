package org.iata.bsplink.refund.loader.validation;

import static org.iata.bsplink.refund.loader.model.record.RecordIdentifier.IT01;
import static org.iata.bsplink.refund.loader.model.record.RecordIdentifier.IT0Z;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Basic refund file validations.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RefundFileValidator {

    private static final int RECORD_LINE_LENGTH = 255;

    private static final String FILE_EMPTY = "The file is empty";
    private static final String IT01_EXPECTED_IN_LINE1 =
            "The IT01 Record is expected to be reported in first line.";
    private static final String IT0Z_EXPECTED_IN_LASTLINE =
            "The IT0Z Record is expected to be reported in last line.";
    private static final String IT0Z_RRDC_INCORRECT_FORMAT =
            "The report record counter doesn't have a valid format.";
    private static final String INCORRECT_NUMBER_OF_RECORDS =
            "Incorrect number of records reported in Report Record Counter field.";

    private static final String LESS_RECORD_LENGTH = "The record has less than 255 characters.";
    private static final String MORE_RECORD_LENGTH = "The record has more than 255 characters.";

    private List<RefundLoaderError> refundLoaderErrors;

    /**
     * Does the file validation.
     */
    public void validate(File file, List<RefundLoaderError> refundLoaderErrors) {

        this.refundLoaderErrors = refundLoaderErrors;

        if (!validateFileSize(file)) {
            return;
        }

        String currentLine = null;
        Optional<String> optionalLastReadLine = Optional.empty();
        int actualLinesCount = 0;

        try (FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader)) {

            while ((currentLine = lineNumberReader.readLine()) != null) {

                optionalLastReadLine = Optional.ofNullable(currentLine);
                actualLinesCount++;

                validateRecordLength(currentLine, actualLinesCount);

                if (actualLinesCount == 1 && ! validateHeaderRecord(currentLine)) {
                    return;
                }
            }

        } catch (IOException exception) {

            throw new RefundLoaderException(exception);
        }

        if (!validateTrailerRecord(optionalLastReadLine, actualLinesCount)) {
            return;
        }

        validateRecordCounter(optionalLastReadLine, actualLinesCount);

    }

    private boolean validateFileSize(File file) {

        if (file.length() > 0) {

            return true;
        }

        addToErrors(null, null, null, FILE_EMPTY);

        return false;
    }

    private void validateRecordLength(String line, int lineNumber) {

        if (line.length() < RECORD_LINE_LENGTH) {

            addToErrors(lineNumber, null, null, LESS_RECORD_LENGTH);

        } else if (line.length() > RECORD_LINE_LENGTH) {

            addToErrors(lineNumber, null, null, MORE_RECORD_LENGTH);
        }
    }

    private boolean validateHeaderRecord(String line) {

        if (line.startsWith("1")) {

            return true;
        }

        addToErrors(1, IT01, "recordIdentifier", IT01_EXPECTED_IN_LINE1);

        return false;
    }


    private boolean validateTrailerRecord(Optional<String> optionalLastReadLine,
            int actualLinesCount) {

        if (optionalLastReadLine.isPresent() && optionalLastReadLine.get().startsWith("Z")) {

            return true;
        }

        addToErrors(actualLinesCount, IT0Z, "recordIdentifier", IT0Z_EXPECTED_IN_LASTLINE);

        return false;
    }

    private boolean validateRecordCounter(Optional<String> optionalLastReadLine,
            Integer actualLinesCount) {

        Integer expectedLinesCount = extractRecordCounter(optionalLastReadLine);

        if (expectedLinesCount == null) {

            addToErrors(actualLinesCount, IT0Z, "reportRecordCounter", IT0Z_RRDC_INCORRECT_FORMAT);

            return false;
        }

        if (!actualLinesCount.equals(expectedLinesCount)) {

            addToErrors(actualLinesCount, IT0Z, "reportRecordCounter", INCORRECT_NUMBER_OF_RECORDS);

            return false;
        }

        return true;
    }

    private Integer extractRecordCounter(Optional<String> optionalLastReadLine) {

        if (optionalLastReadLine.isPresent()) {

            String recordCount = optionalLastReadLine.get().substring(2, 12);

            try {
                return Integer.parseInt(recordCount);
            } catch (NumberFormatException exception) {
                // do nothing
            }
        }

        return null;
    }


    private void addToErrors(Integer lineNumber, RecordIdentifier recordIdentifier, String field,
            String message) {

        RefundLoaderError error = new RefundLoaderError();

        error.setLineNumber(lineNumber);
        error.setRecordIdentifier(recordIdentifier);
        error.setField(field);
        error.setMessage(message);
        error.setValidationPhase(ValidationPhase.FILE);

        refundLoaderErrors.add(error);
    }

    public List<RefundLoaderError> getErrors() {

        return refundLoaderErrors;
    }

}
