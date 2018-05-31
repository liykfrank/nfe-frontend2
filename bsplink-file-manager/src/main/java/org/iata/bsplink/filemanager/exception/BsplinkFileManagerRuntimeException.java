package org.iata.bsplink.filemanager.exception;

public class BsplinkFileManagerRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BsplinkFileManagerRuntimeException() {
        super();
    }

    public BsplinkFileManagerRuntimeException(String message) {
        super(message);
    }

    public BsplinkFileManagerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BsplinkFileManagerRuntimeException(Throwable cause) {
        super(cause);
    }
}
