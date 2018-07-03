package org.iata.bsplink.filemanager.exception;

/**
 * Custom exception class.
 * @author danilo.rodas
 *
 */
public class BsplinkFileManagerException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public BsplinkFileManagerException() {
        super();
    }

    public BsplinkFileManagerException(String message) {
        super(message);
    }

    public BsplinkFileManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BsplinkFileManagerException(Throwable cause) {
        super(cause);
    }   
   
}
