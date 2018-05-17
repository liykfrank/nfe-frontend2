package org.iata.bsplink.filemanager.pojo;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@ToString
public class ResourceSearchCriteria {

    private Long id;
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant minCreatedInstant;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant maxCreatedInstant;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant minModifiedInstant;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant maxModifiedInstant;
}
