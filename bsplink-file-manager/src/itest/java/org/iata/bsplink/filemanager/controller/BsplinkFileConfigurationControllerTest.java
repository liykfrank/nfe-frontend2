package org.iata.bsplink.filemanager.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.pojo.BsplinkFileConfiguration;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BsplinkFileConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BsplinkFileConfigService bsplinkFileConfigurationService;

    @Autowired
    private MultipartProperties multipartProperties;
    
    @Autowired
    protected WebApplicationContext webAppContext;


    @Before
    public void setUp() throws IOException, BsplinkValidationException {
        
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
        
        bsplinkFileConfigurationService.update(testConfiguration().getBsplinkFileBasicConfig());
    }

    @Test
    public void testGetConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testConfiguration());

        mockMvc.perform(get("/v1/configurations"))
            .andExpect(status().isOk())
            .andExpect(content().json(json, true));
    }

    @Test
    public void testUpdateConfig() throws Exception {
        BsplinkFileConfiguration bfc = testPutConfiguration();
        BsplinkFileBasicConfig cfg = bfc.getBsplinkFileBasicConfig();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bfc);

        mockMvc.perform(put("/v1/configurations").content(json)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertEquals(cfg, bsplinkFileConfigurationService.find());
    }

    @Test()
    public void testUpdateConfigErrors() throws Exception {
        BsplinkFileConfiguration bfc = testPutConfiguration();
        BsplinkFileBasicConfig cfg = bfc.getBsplinkFileBasicConfig();

        cfg.setAllowedFileExtensions(null);
        cfg.setMaxDownloadFilesNumber(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bfc);

        mockMvc.perform(put("/v1/configurations")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("maxDownloadFilesNumber must not be null")))
                .andExpect(jsonPath("$.error",
                        containsString("allowedFileExtensions must not be null")));

        assertEquals(testConfiguration().getBsplinkFileBasicConfig(),
                bsplinkFileConfigurationService.find());
    }

    @Test()
    public void testUpdateConfigNullValues() throws Exception {
        BsplinkFileConfiguration bfc = testPutConfiguration();
        BsplinkFileBasicConfig cfg = bfc.getBsplinkFileBasicConfig();

        cfg.getAllowedFileExtensions().add(null);
        cfg.getFileNamePatterns().add(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bfc);

        mockMvc.perform(put("/v1/configurations").content(json)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString(BsplinkFileConfigService.ERROR_MSG_PATTERN_NULL)))
                .andExpect(jsonPath("$.error",
                        containsString(BsplinkFileConfigService.ERROR_MSG_EXTENSION_NULL)));

        assertEquals(testConfiguration().getBsplinkFileBasicConfig(),
                bsplinkFileConfigurationService.find());
    }

    @Test()
    public void testUpdateConfigInvalidPattern() throws Exception {
        BsplinkFileConfiguration bfc = testPutConfiguration();
        BsplinkFileBasicConfig cfg = bfc.getBsplinkFileBasicConfig();

        cfg.getFileNamePatterns().add("*.[");
        cfg.getFileNamePatterns().add("abc");
        cfg.getFileNamePatterns().add("][");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bfc);

        mockMvc.perform(
                put("/v1/configurations").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString(BsplinkFileConfigService.ERROR_MSG_SYNTAX)))
                .andExpect(jsonPath("$.error", containsString("*.[")))
                .andExpect(jsonPath("$.error", containsString("][")));

        assertEquals(testConfiguration().getBsplinkFileBasicConfig(),
                bsplinkFileConfigurationService.find());
    }

    private BsplinkFileConfiguration testConfiguration() {
        BsplinkFileBasicConfig cfg =  new BsplinkFileBasicConfig();
        List<String> fileNamePatterns = new ArrayList<>(2);
        fileNamePatterns.add("[a-z0-9_.]*");
        cfg.setFileNamePatterns(fileNamePatterns);

        List<String> fileExtensions = new ArrayList<>(2);
        fileExtensions.add("txt");
        cfg.setAllowedFileExtensions(fileExtensions);
        cfg.setMaxDownloadTotalFileSizeForMultipleFiles(345L);

        cfg.setMaxUploadFilesNumber(8);
        cfg.setMaxDownloadFilesNumber(7);
        BsplinkFileConfiguration bfc = new BsplinkFileConfiguration();
        bfc.setBsplinkFileBasicConfig(cfg);
        bfc.setMaxUploadFileSize(multipartProperties.getMaxFileSize());
        return bfc;
    }

    private BsplinkFileConfiguration testPutConfiguration() {
        BsplinkFileBasicConfig cfg =  new BsplinkFileBasicConfig();
        List<String> fileNamePatterns = new ArrayList<>(2);
        fileNamePatterns.add("X");
        fileNamePatterns.add("Y");
        cfg.setFileNamePatterns(fileNamePatterns);

        List<String> fileExtensions = new ArrayList<>(2);
        fileExtensions.add("kkk");
        fileExtensions.add("xxx");
        cfg.setAllowedFileExtensions(fileExtensions);

        cfg.setMaxDownloadTotalFileSizeForMultipleFiles(2L);

        cfg.setMaxUploadFilesNumber(3);
        cfg.setMaxDownloadFilesNumber(4);

        BsplinkFileConfiguration bfc = new BsplinkFileConfiguration();
        bfc.setBsplinkFileBasicConfig(cfg);
        bfc.setMaxUploadFileSize(multipartProperties.getMaxFileSize());
        return bfc;
    }
}
