package org.iata.bsplink.filemanager.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.iata.bsplink.filemanager.configuration.ApplicationConfiguration;
import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.configuration.SecurityConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.iata.bsplink.filemanager.utils.BsplinkFileUtils;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
public class BsplinkFileControllerDownloadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigurationService;

    @MockBean
    private YadeUtils yadeUtils;

    @Value("${app.local_downloaded_files_directory}")
    private String localDownloadedFilesDirectory;

    private Path uploadedFilesDirectory;

    private static File dirUploadedFiles;
    
    @Autowired
    protected WebApplicationContext webAppContext;

    @Before
    public void setUp() throws IOException, BsplinkValidationException {
        
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
        
        File uploadFolder = new File(applicationConfiguration.getLocalUploadedFilesDirectory());
        if (uploadFolder.exists()) {
            FileUtils.cleanDirectory(uploadFolder);
        } else {
            uploadFolder.mkdir();
        }
        bsplinkFileConfigurationService.update(testConfiguration());
    }

    @After
    public void tearDown() throws IOException {
        File uploadFolder = new File(applicationConfiguration.getLocalUploadedFilesDirectory());
        FileUtils.forceDelete(uploadFolder);
    }

    @Before
    public void seetUpForFiles() throws IOException {

        uploadedFilesDirectory = Paths.get(localDownloadedFilesDirectory);
        dirUploadedFiles = new File(uploadedFilesDirectory.toString());

        if (dirUploadedFiles.exists()) {
            FileUtils.deleteDirectory(dirUploadedFiles);
        }

        dirUploadedFiles.mkdir();

        List<BsplinkFile> bsFileList = getFilesSftp();

        for (BsplinkFile bsplinkFile : bsFileList) {
            File f = new File(
                    localDownloadedFilesDirectory + File.separator + bsplinkFile.getName());
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
    }

    @AfterClass
    public static void doYourOneTimeTeardown() throws IOException {

        if (dirUploadedFiles.exists()) {
            FileUtils.deleteDirectory(dirUploadedFiles);
        }
    }

    private String getBspinkFileStatus(Long id) {

        return bsplinkFileRepository.findById(id).get().getStatus().toString();
    }


    @Test
    public void testDownloadSingleFile() throws Exception {

        mockMvc.perform(get("/v1/files/1")).andExpect(status().isOk());
        assertThat(getBspinkFileStatus(1L), equalTo(BsplinkFileStatus.DOWNLOADED.toString()));

    }

    @Test
    public void testDownloadSingleFileNotFound() throws Exception {

        mockMvc.perform(get("/v1/files/1335")).andExpect(status().isNotFound());

    }

    @Test
    public void testDownloadSingleFileBadRequest() throws Exception {

        mockMvc.perform(get("/v1/files/notValidId")).andExpect(status().isBadRequest());

    }

    @Test
    public void testDownloadSingleFileThrowsException() throws Exception {

        mockMvc.perform(get("/v1/files/15")).andExpect(status().isInternalServerError());

    }

    @Test
    public void testDownloadZipFileOneFile() throws Exception {

        mockMvc.perform(get("/v1/files/zip?id=1")).andExpect(status().isOk());

        assertThat(getBspinkFileStatus(1L), equalTo(BsplinkFileStatus.DOWNLOADED.toString()));
    }

    @Test
    public void testDownloadZipFileSeveralFiles() throws Exception {

        mockMvc.perform(get("/v1/files/zip?id=1&id=2&id=3")).andExpect(status().isOk());

        assertThat(getBspinkFileStatus(1L), equalTo(BsplinkFileStatus.DOWNLOADED.toString()));
        assertThat(getBspinkFileStatus(2L), equalTo(BsplinkFileStatus.DOWNLOADED.toString()));
        assertThat(getBspinkFileStatus(3L), equalTo(BsplinkFileStatus.DOWNLOADED.toString()));
    }

    @Test
    public void testDownloadZipFileBadRequest() throws Exception {

        mockMvc.perform(get("/v1/files/zip")).andExpect(status().isBadRequest());

    }

    @Test
    public void testDownloadZipFileNotFound() throws Exception {

        mockMvc.perform(get("/v1/files/zip?id=100")).andExpect(status().isNotFound());

    }

    @Test
    public void testDownloadZipFileThrowsException() throws Exception {

        mockMvc.perform(get("/v1/files/zip?id=15666")).andExpect(status().isNotFound());

    }

    @Test
    public void testDownloadZipFileNumberExceeded() throws Exception {
        BsplinkFileBasicConfig cfg = testConfiguration();
        cfg.setMaxDownloadFilesNumber(2);
        bsplinkFileConfigurationService.update(cfg);

        mockMvc.perform(get("/v1/files/zip?id=1&id=3&id=5")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString(BsplinkFileUtils.ERROR_MSG_NUMBER_EXCEEDED)));
    }

    @Test
    public void testDownloadZipFileSizeExceeded() throws Exception {
        BsplinkFileBasicConfig cfg = testConfiguration();
        cfg.setMaxDownloadTotalFileSizeForMultipleFiles(1L);
        bsplinkFileConfigurationService.update(cfg);

        mockMvc.perform(get("/v1/files/zip?id=1&id=3&id=5")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString(BsplinkFileUtils.ERROR_MSG_SIZE_EXCEEDED)));
    }

    @Test
    public void testDownloadZipFileNumberExceededDisabled() throws Exception {
        BsplinkFileBasicConfig cfg = testConfiguration();
        cfg.setMaxDownloadFilesNumber(-2);
        bsplinkFileConfigurationService.update(cfg);

        mockMvc.perform(get("/v1/files/zip?id=1&id=2&id=3&id=4")).andExpect(status().isOk());
    }

    @Test
    public void testDownloadZipFileSizeExceededDisabled() throws Exception {
        BsplinkFileBasicConfig cfg = testConfiguration();
        cfg.setMaxDownloadTotalFileSizeForMultipleFiles(-1L);
        bsplinkFileConfigurationService.update(cfg);

        mockMvc.perform(get("/v1/files/zip?id=1&id=2&id=3&id=4"))
                .andExpect(status().isBadRequest());
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

    private List<BsplinkFile> getFilesSftp() {

        BsplinkFile file1 = new BsplinkFile();
        file1.setId(1L);
        file1.setName("ILei8385_20180128_file1");
        file1.setStatus(BsplinkFileStatus.DELETED);
        file1.setType("fileType1");
        file1.setUploadDateTime(Instant.parse("2018-01-01T00:00:00Z"));
        file1.setBytes(1001L);

        BsplinkFile file2 = new BsplinkFile();
        file2.setId(2L);
        file2.setName("ILei8385_20180128_file2");
        file2.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        file2.setType("fileType2");
        file2.setUploadDateTime(Instant.parse("2018-01-02T00:00:00Z"));
        file2.setBytes(1002L);

        BsplinkFile file3 = new BsplinkFile();
        file3.setId(3L);
        file3.setName("ILei8385_20180128_file3");
        file3.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        file3.setType("fileType2");
        file3.setUploadDateTime(Instant.parse("2018-01-02T00:00:00Z"));
        file3.setBytes(1003L);

        BsplinkFile file4 = new BsplinkFile();
        file4.setId(4L);
        file4.setName("ILei8385_20180128_file4");
        file4.setStatus(BsplinkFileStatus.DOWNLOADED);
        file4.setType("fileType4");
        file4.setUploadDateTime(Instant.parse("2018-01-02T00:00:00Z"));
        file4.setBytes(1004L);

        List<BsplinkFile> listbsplinkFiles = new ArrayList<>();
        listbsplinkFiles.add(file1);
        listbsplinkFiles.add(file2);
        listbsplinkFiles.add(file3);
        listbsplinkFiles.add(file4);

        return listbsplinkFiles;
    }

}
