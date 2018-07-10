package org.iata.bsplink.refund.loader.validation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Optional;

import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.exception.WrongFileFormatException;
import org.iata.bsplink.refund.loader.exception.WrongRecordCounterException;
import org.springframework.stereotype.Component;

/**
 * Basic refund file validations.
 */
@Component
public class RefundFileValidator {

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

            throw new WrongRecordCounterException(actualLinesCount, expectedLinesCount);
        }

    }

    private void throwExeptionIfFileIsEmpty(File file) {

        if (file.length() <= 0) {

            throw new WrongFileFormatException("the file is empty");
        }

    }

    private void throwExceptionIfThereIsNoFileHeaderRecord(String line) {

        if (!line.startsWith("1")) {

            throw new WrongFileFormatException("the file does not have header record (IT01)");
        }
    }

    private void throwExceptionIfThereIsNoFileTrailerRecord(Optional<String> optionalLastReadLine) {

        if (!(optionalLastReadLine.isPresent() && optionalLastReadLine.get().startsWith("Z"))) {

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

        // if the record count was not returned yet there is a problem extracting the value
        throw new WrongFileFormatException("the record count can not be extracted from file");
    }

}
