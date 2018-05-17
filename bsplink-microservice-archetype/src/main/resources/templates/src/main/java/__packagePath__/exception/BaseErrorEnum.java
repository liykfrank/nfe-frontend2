package @packageName@.exception;

import org.springframework.http.HttpStatus;

public enum BaseErrorEnum {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "001",
            "Unexpected error happened: {0}"),

    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "002", "Resource with id {0} not found");

    private HttpStatus httpStatus;

    private String errorCode;

    private String description;

    private BaseErrorEnum(HttpStatus httpStatus, String errorCode, String description) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.description = description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
