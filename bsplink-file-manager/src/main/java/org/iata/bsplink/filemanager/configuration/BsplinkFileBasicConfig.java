package org.iata.bsplink.filemanager.configuration;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BsplinkFileBasicConfig {
    @NotNull(message = "fileNamePatterns must not be null")
    @ApiModelProperty(value = "Regular expressions for BSPlink Filenaming Convention")
    private List<String> fileNamePatterns;

    @NotNull(message = "allowedFileExtensions must not be null")
    @ApiModelProperty(value = "Allowed file extensions in File Download/ Upload.")
    private List<String> allowedFileExtensions;

    @NotNull(message = "maxDownloadTotalFileSizeForMultipleFiles must not be null")
    @ApiModelProperty(value = "Total maximum file size for Multiple File Download.")
    private Long maxDownloadTotalFileSizeForMultipleFiles;

    @NotNull(message = "maxUploadFilesNumber must not be null")
    @ApiModelProperty(value = "The maximum allowed number of files in File Upload.")
    private Integer maxUploadFilesNumber;

    @NotNull(message = "maxDownloadFilesNumber must not be null")
    @ApiModelProperty(value = "The maximum allowed number of files in File Download.")
    private Integer maxDownloadFilesNumber;
}
