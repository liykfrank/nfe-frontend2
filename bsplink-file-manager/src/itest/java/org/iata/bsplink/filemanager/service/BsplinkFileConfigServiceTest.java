package org.iata.bsplink.filemanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BsplinkFileConfigServiceTest {

    @Autowired
    BsplinkFileConfigService bsplinkFileConfigurationService;

    @Test
    public void testFind() throws Exception {
        BsplinkFileBasicConfig bsplinkFileConfiguration = bsplinkFileConfigurationService.find();
        assertNotNull(bsplinkFileConfiguration);
    }

    @Test
    public void testUpdate() throws Exception {
        BsplinkFileBasicConfig newBsplinkFileConfiguration = new BsplinkFileBasicConfig();

        List<String> fileNamePatterns = new ArrayList<>(2);
        fileNamePatterns.add("A");
        fileNamePatterns.add("B");
        newBsplinkFileConfiguration.setFileNamePatterns(fileNamePatterns);

        List<String> fileExtensions = new ArrayList<>(2);
        fileExtensions.add("abc");
        fileExtensions.add("def");
        newBsplinkFileConfiguration.setAllowedFileExtensions(fileExtensions);

        newBsplinkFileConfiguration.setMaxDownloadTotalFileSizeForMultipleFiles(345L);

        newBsplinkFileConfiguration.setMaxUploadFilesNumber(8);
        newBsplinkFileConfiguration.setMaxDownloadFilesNumber(7);

        bsplinkFileConfigurationService.update(newBsplinkFileConfiguration);

        BsplinkFileBasicConfig bsplinkFileConfiguration = bsplinkFileConfigurationService.find();

        assertEquals(bsplinkFileConfiguration, newBsplinkFileConfiguration);
    }

    @Test(expected = BsplinkValidationException.class)
    public void testPatternValidation() throws Exception {
        BsplinkFileBasicConfig bsplinkFileConfiguration = bsplinkFileConfigurationService.find();
        bsplinkFileConfiguration.getFileNamePatterns().add("*abc[");
        bsplinkFileConfigurationService.update(bsplinkFileConfiguration);
    }

    @Test(expected = BsplinkValidationException.class)
    public void testPatternNullValidation() throws Exception {
        BsplinkFileBasicConfig bsplinkFileConfiguration = bsplinkFileConfigurationService.find();
        bsplinkFileConfiguration.getFileNamePatterns().add(null);
        bsplinkFileConfigurationService.update(bsplinkFileConfiguration);
    }

    @Test(expected = BsplinkValidationException.class)
    public void testExtensionsNullValidation() throws Exception {
        BsplinkFileBasicConfig bsplinkFileConfiguration = bsplinkFileConfigurationService.find();
        bsplinkFileConfiguration.getAllowedFileExtensions().add(null);
        bsplinkFileConfigurationService.update(bsplinkFileConfiguration);
    }
}
