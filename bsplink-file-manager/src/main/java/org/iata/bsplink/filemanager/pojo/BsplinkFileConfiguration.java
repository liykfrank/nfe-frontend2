package org.iata.bsplink.filemanager.pojo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;

/**
 * BSPlink File-Manager Configuration.
 */
@Data
@ApiModel(description = "Configurations for File Manager")
public class BsplinkFileConfiguration {

    @NotNull
    @Valid
    @JsonUnwrapped
    private BsplinkFileBasicConfig bsplinkFileBasicConfig;

    @ApiModelProperty(value = "Maximum file size for File Upload. (Read-only)")
    private String maxUploadFileSize;
}
