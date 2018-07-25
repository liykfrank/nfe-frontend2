package org.iata.bsplink.agencymemo.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public FeignClientException(HttpStatus httpStatus) {
        super(httpStatus.name());
        this.httpStatus = httpStatus;
    }
}
