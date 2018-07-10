package org.iata.bsplink.refund.loader.exception;

public class WrongFileFormatException extends RefundLoaderException {

    private static final long serialVersionUID = 1L;

    public WrongFileFormatException(String message) {
        super(message);
    }

}
