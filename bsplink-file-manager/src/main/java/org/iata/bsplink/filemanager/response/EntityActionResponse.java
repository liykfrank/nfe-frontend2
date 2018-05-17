package org.iata.bsplink.filemanager.response;

import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import org.springframework.http.HttpStatus;

@Data
public class EntityActionResponse<IdT> {

    @ApiModelProperty(value = "Resource identifier")
    private IdT id;
    @ApiModelProperty(value = "HTTP status code that represents the action's result")
    private Integer status;
    @ApiModelProperty(value = "Result description")
    private String message;

    /**
     * Creates a new response using the given HTTP status.
     */
    public EntityActionResponse(IdT id, HttpStatus httpStatus) {

        this(id, httpStatus, httpStatus.name());
    }

    /**
     * Creates a new response with a specific message.
     */
    public EntityActionResponse(IdT id, HttpStatus httpStatus, String message) {

        this.id = id;
        this.status = httpStatus.value();
        this.message = message;
    }
}
