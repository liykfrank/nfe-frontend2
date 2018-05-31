package org.iata.bsplink.filemanager.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@CommonsLog
public class FileStartupConfiguration {

    @Value("${app.yade.outbox}")
    private String yadeOutbox;

    @Autowired
    private Environment environment;

    /**
     * Creates files when local profile is activated.
     * 
     * @throws IOException Error in creation of directory or file.
     */
    @PostConstruct
    public void createFilesForLocalProfile() throws IOException {
        if (Arrays.stream(environment.getActiveProfiles())
                .anyMatch(env -> (env.equalsIgnoreCase("test")))) {

            Path uploadedFilesDirectory = Paths.get(yadeOutbox);

            File dirUploadedFiles = new File(uploadedFilesDirectory.toString());

            if (!dirUploadedFiles.exists() && dirUploadedFiles.mkdir()) {

                log.info("Directory " + dirUploadedFiles + " created.");

                for (int i = 1; i <= 10; i++) {

                    File f = new File(uploadedFilesDirectory + File.separator + "file" + i);
                    f.getParentFile().mkdirs();

                    if (f.createNewFile()) {
                        log.info("File: " + "file" + i + " created.");
                    }
                }

            }
        }
    }
}
