package org.iata.bsplink.refund.loader.exception;

/**
 * Threw when the number of lines doesn't match the record count of the IT0Z record.
 */
public class WrongRecordCounterException extends RefundLoaderException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the exception with the actual number of lines and the number of lines expected.
     */
    public WrongRecordCounterException(int actualLinesCount, int expectedLinesCount) {

        super(String.format("wrong number of file lines: expected %d there are %d",
                expectedLinesCount, actualLinesCount));
    }
}
