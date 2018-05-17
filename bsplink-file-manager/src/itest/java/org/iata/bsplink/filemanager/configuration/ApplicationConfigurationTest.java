package org.iata.bsplink.filemanager.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.local_uploaded_files_directory=my-temp-directory",
        "app.file_name_rex=xxx"})
@ActiveProfiles("test")
public class ApplicationConfigurationTest {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Test
    public void testApplicationConfiguration() throws Exception {

        assertEquals("my-temp-directory",
                applicationConfiguration.getLocalUploadedFilesDirectory());
    }
}
