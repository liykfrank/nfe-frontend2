package org.iata.bsplink.filemanager.model.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.converter.JpaJsonBsplinkConfigConverter;

@Data
@NoArgsConstructor
@Entity
@Table
public class BsplinkConfig {

    @Id
    private String id;

    @Column(nullable = false)
    @NonNull
    @Convert(converter = JpaJsonBsplinkConfigConverter.class)
    private BsplinkFileBasicConfig config;
}
