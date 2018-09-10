package org.iata.bsplink.filemanager.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.iata.bsplink.filemanager.service.FileAccessPermissionService;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/fixtures/sql/user_files.sql")
public class BsplinkFileControllerTrashTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YadeUtils yadeUtils;

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigurationService;

    @Autowired
    protected WebApplicationContext webAppContext;

    @MockBean
    private Principal principal;

    @MockBean
    private FileAccessPermissionService fileAccessPermissionService;

    @Before
    public void setUp() throws IOException, BsplinkValidationException {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();

        bsplinkFileRepository.deleteAll();
        bsplinkFileConfigurationService.update(testConfiguration());
    }


    private BsplinkFileBasicConfig testConfiguration() {
        BsplinkFileBasicConfig cfg = new BsplinkFileBasicConfig();
        List<String> fileNamePatterns = new ArrayList<>(2);
        fileNamePatterns.add("[a-z0-9_.]*");
        cfg.setFileNamePatterns(fileNamePatterns);

        List<String> fileExtensions = new ArrayList<>(2);
        fileExtensions.add("txt");
        cfg.setAllowedFileExtensions(fileExtensions);

        cfg.setMaxDownloadTotalFileSizeForMultipleFiles(8000000000L);

        cfg.setMaxUploadFilesNumber(8);
        cfg.setMaxDownloadFilesNumber(3);

        return cfg;
    }

    @Test
    public void testDeletedAndTrashed() throws Exception {
        BsplinkFile bsplinkFile1 = new BsplinkFile();
        bsplinkFile1.setName("ESxx2203_20181212_test15376.txt");
        bsplinkFile1.setType("xx");
        bsplinkFile1.setBytes(1212L);
        bsplinkFile1.setStatus(BsplinkFileStatus.DELETED);
        bsplinkFile1.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile1);

        BsplinkFile bsplinkFile2 = new BsplinkFile();
        bsplinkFile2.setName("ESxx2203_20181212_test15376.txt");
        bsplinkFile2.setType("xx");
        bsplinkFile2.setBytes(2424L);
        bsplinkFile2.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        bsplinkFile2.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile2);

        Long id1 = bsplinkFile1.getId();
        Long id2 = bsplinkFile2.getId();

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                any(), any(), any()))
                .thenReturn(true);

        mockMvc.perform(delete("/v1/files/" + id2).principal(principal))
                .andExpect(status().isOk());

        assertEquals(BsplinkFileStatus.TRASHED,
                bsplinkFileRepository.findById(id1).get().getStatus());

        assertEquals(BsplinkFileStatus.DELETED,
                bsplinkFileRepository.findById(id2).get().getStatus());
    }

    @Test
    public void testFilesListWithoutTrashedFiles() throws Exception {
        BsplinkFile bsplinkFile1 = new BsplinkFile();
        bsplinkFile1.setName("ESab2203_20180518_trash.txt");
        bsplinkFile1.setType("ab");
        bsplinkFile1.setBytes(4L);
        bsplinkFile1.setStatus(BsplinkFileStatus.TRASHED);
        bsplinkFile1.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile1);

        BsplinkFile bsplinkFile2 = new BsplinkFile();
        bsplinkFile2.setName("ESab2203_20180518_sent.txt");
        bsplinkFile2.setType("ab");
        bsplinkFile2.setBytes(43L);
        bsplinkFile2.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        bsplinkFile2.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile2);

        int id2 = (int) bsplinkFile2.getId().longValue();

        mockMvc.perform(get("/v1/files")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", equalTo(id2)));
    }

}
