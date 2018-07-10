package org.iata.bsplink.refund.loader.exception;

public class RefundLoaderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RefundLoaderException(String message) {
        super(message);
    }

    public RefundLoaderException(Throwable cause) {
        super(cause);
    }

}
