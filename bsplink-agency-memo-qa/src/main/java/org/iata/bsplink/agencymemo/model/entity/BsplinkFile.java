package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@Embeddable
@NoArgsConstructor
@Table(name = "bsplink_file")
public class BsplinkFile {

    @Id
    @GeneratedValue
    @ApiModelProperty(value = "Identificator")
    private Long id;

    @NonNull
    @Column(nullable = false)
    @ApiModelProperty(value = "Name")
    private String name;

    @ApiModelProperty(value = "Path to file in server")
    private String path;

    @NonNull
    @Column(nullable = false)
    @ApiModelProperty(value = "File size in bytes")
    private Long bytes;

    @NonNull
    @ApiModelProperty(
            value = "File upload date and time in UTC ISO format (e.g: 2018-01-01T00:00:00Z)")
    @Column(name = "upload_date_time", nullable = false)
    private Instant uploadDateTime;

    @NonNull
    @JsonIgnore
    @ApiModelProperty(value = "Id for ACDM")
    @Column(name = "acdm_id", nullable = false)
    private Long acdmId;

}
