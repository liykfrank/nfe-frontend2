package org.iata.bsplink.filemanager.pojo;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
public class BsplinkFileSearchCriteria {

    private Long id;
    private String name;
    private String type;
    private BsplinkFileStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant minUploadDateTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant maxUploadDateTime;

    private Long minBytes;
    private Long maxBytes;
}
