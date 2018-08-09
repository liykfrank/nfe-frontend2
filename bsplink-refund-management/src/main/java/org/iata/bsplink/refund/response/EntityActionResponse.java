package org.iata.bsplink.refund.response;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityActionResponse<I> {

    @ApiModelProperty(value = "Resource identifier")
    private I id;
    @ApiModelProperty(value = "HTTP status code that represents the action's result")
    private Integer status;
    @ApiModelProperty(value = "Result description")
    private String message;

    /**
     * Creates a new response using the given HTTP status.
     */
    public EntityActionResponse(I id, HttpStatus httpStatus) {

        this(id, httpStatus, httpStatus.name());
    }

    /**
     * Creates a new response with a specific message.
     */
    public EntityActionResponse(I id, HttpStatus httpStatus, String message) {

        this.id = id;
        this.status = httpStatus.value();
        this.message = message;
    }
}
