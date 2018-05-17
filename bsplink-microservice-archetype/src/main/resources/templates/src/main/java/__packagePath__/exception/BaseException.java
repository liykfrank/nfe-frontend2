package @packageName@.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
@Setter
@ToString(callSuper = false)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -4113271722818742363L;

    private HttpStatus httpStatus;
    private String description;
    private String errorCode;

    public BaseException(final BaseErrorEnum type, final Object value) {
        this(type, type.getHttpStatus(), type.getErrorCode(), type.getDescription(), value);
    }

    /**
     * Instantiates a new base exception.
     *
     * @param errorEnum the error enum
     * @param httpStatus the http status
     * @param errorCode the error code
     * @param description the description of the exception
     * @param value the value to be replaced in the error description
     */
    public BaseException(final BaseErrorEnum errorEnum, final HttpStatus httpStatus,
            final String errorCode, final String description, final Object value) {
        super(description.replace("{0}", String.valueOf(value)));

        setErrorEnum(errorEnum);
        setHttpStatus(httpStatus);
        setErrorCode(errorCode);
        setDescription(description.replace("{0}", String.valueOf(value)));
    }

    /**
     * Instantiates a new base exception.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     * @param errorEnum the error enum
     */
    public BaseException(String message, Throwable cause, BaseErrorEnum errorEnum) {
        super(message, cause);
        setHttpStatusCode(cause);
        setErrorEnum(errorEnum);
        setDescription(message);
    }

    /**
     * Sets the http status and the description matching the provided error enum.
     *
     * @param errorEnum the error enum
     */
    public void setErrorEnum(BaseErrorEnum errorEnum) {
        if (errorEnum != null) {
            setHttpStatus(errorEnum.getHttpStatus());
            setDescription(errorEnum.getDescription());
        }
    }

    /**
     * Sets the http status code when required.
     *
     * @param cause the cause of the exception
     */
    private void setHttpStatusCode(Throwable cause) {
        if (cause instanceof HttpStatusCodeException) {
            HttpStatusCodeException restE = (HttpStatusCodeException) cause;
            setHttpStatus(restE.getStatusCode());
        }
    }
}
