package org.iata.bsplink.filemanager.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exception for validation.
 */
public class BsplinkValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public BsplinkValidationException(List<String> errors) {
        super(String.join(", ", errors));
    }

    /**
     * Returns the error message in an map.
     */
    public Map<String, String> getErrorMap() {
        Map<String, String> error = new HashMap<>();
        error.put("error", getMessage());
        return error;
    }
}
