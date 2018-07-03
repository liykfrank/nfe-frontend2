package org.iata.bsplink.filemanager.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table
@ApiModel(description = "BSPlink file")
public class BsplinkFile {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    private String type;

    @Column(nullable = false)
    @NonNull
    @ApiModelProperty(value = "File size in bytes")
    private Long bytes;

    @Column(nullable = false)
    @NonNull
    @ApiModelProperty(
            value = "File upload date and time in UTC ISO format (e.g: 2018-01-01T00:00:00Z)")
    private Instant uploadDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BsplinkFileStatus status = BsplinkFileStatus.NOT_DOWNLOADED;
}
