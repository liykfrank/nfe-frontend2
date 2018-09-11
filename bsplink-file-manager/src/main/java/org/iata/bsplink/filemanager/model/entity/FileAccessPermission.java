package org.iata.bsplink.filemanager.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Entity
@Data
public class FileAccessPermission {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "user_id", length = 36)
    private String user;

    @ApiModelProperty(value = "ISO Country Code, two letter code", required = true)
    @NotNull
    @Column(length = 2)
    @Pattern(regexp = "[A-Z][A-Z0-9]")
    private String isoCountryCode;

    @ApiModelProperty(value = "File descriptor, two letter code", required = true)
    @NotNull
    @Column(length = 2)
    @Pattern(regexp = "[a-z0-9]{2}")
    private String fileType;

    @ApiModelProperty(value = "File Access Type (DOWNLOAD/ UPLOAD)", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private FileAccessType access;
}
