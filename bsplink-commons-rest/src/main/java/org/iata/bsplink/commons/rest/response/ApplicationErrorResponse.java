package org.iata.bsplink.commons.rest.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@JsonPropertyOrder({ "timestamp", "status", "error", "message", "path" })
public class ApplicationErrorResponse {

    private Long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApplicationErrorResponse() { }

    /**
     * Application error response body.
     */
    public ApplicationErrorResponse(String message, HttpStatus httpStatus, String path) {

        this.timestamp = Instant.now().toEpochMilli();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
