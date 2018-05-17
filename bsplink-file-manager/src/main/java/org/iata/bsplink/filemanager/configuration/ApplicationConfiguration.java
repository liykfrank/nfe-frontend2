package org.iata.bsplink.filemanager.configuration;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
public class ApplicationConfiguration {

    private String localUploadedFilesDirectory;
}
