package org.iata.bsplink.filemanager.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;

@Getter
@Setter
@ApiModel(description = "Simple Response")
public class SimpleResponse extends EntityActionResponse<Long> {
    public SimpleResponse(Long id, HttpStatus httpStatus) {
        super(id, httpStatus);
    }

    public SimpleResponse(Long id, HttpStatus httpStatus, String message) {
        super(id, httpStatus, message);
    }

    @ApiModelProperty(value = "Subject")
    private String subject;
    
    @ApiModelProperty(value = "Resource location")
    private String path;
}
