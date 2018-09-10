package org.iata.bsplink.filemanager.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.iata.bsplink.filemanager.configuration.ApplicationConfiguration;
import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.iata.bsplink.filemanager.service.FileAccessPermissionService;
import org.iata.bsplink.filemanager.service.MultipartFileService;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/fixtures/sql/user_files.sql")
public class BsplinkFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigurationService;

    @MockBean
    private YadeUtils yadeUtils;

    @Autowired
    protected WebApplicationContext webAppContext;

    @MockBean
    private FileAccessPermissionService fileAccessPermissionService;

    @MockBean
    private Principal principal;


    @Before
    public void setUp() throws IOException, BsplinkValidationException {

        MockitoAnnotations.initMocks(this);

        when(fileAccessPermissionService.isFileAccessPermittedForUser(any(), any(), any()))
                .thenReturn(true);
        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(any(), any(), any()))
                .thenReturn(true);
        when(fileAccessPermissionService.isBsplinkFilesAccessPermittedForUser(any(), any(), any()))
                .thenReturn(true);

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

    @Test
    public void testReturnsJsonFileList() throws Exception {

        mockMvc.perform(get("/v1/files?sort=id,asc")).andExpect(status().isOk())
                .andExpect(content().json(getExpectedJsonFileList(), true));
    }

    @Test
    public void testSend() throws Exception {

        String fileName = "bsplink_file_controller_test.txt";
        byte[] fileContent = "abcdefghi".getBytes();

        Path path = Paths.get(applicationConfiguration.getLocalUploadedFilesDirectory(), fileName);

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", fileName, "text/plain", fileContent);

        mockMvc.perform(multipart("/v1/files").file(multipartFile).principal(principal))
                .andExpect(status().isMultiStatus()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].subject", equalTo(fileName)))
                .andExpect(jsonPath("$[0].status", equalTo(HttpStatus.OK.value())));

        assertEquals(true, Files.exists(path));

        assertEquals(Files.size(path), fileContent.length);
    }

    @Test
    public void testSendNoFiles() throws Exception {
        mockMvc.perform(multipart("/v1/files")).andExpect(status().isBadRequest())
                .andExpect(content().json("[]", true));
    }

    @Test
    public void testSendMultiple() throws Exception {

        String[] fileNames = {"bsplink_file_controller_test1.txt",
            "bsplink_file_controller_test2.pdf", "bsplink_file_controller_test3.txt"};

        String[] fileTextContents = {"abcdefghi", "XXX", ""};

        Path path = Paths.get(applicationConfiguration.getLocalUploadedFilesDirectory());

        MockMultipartFile[] multipartFiles = {
            new MockMultipartFile("file", fileNames[0], "text/plain",
                        fileTextContents[0].getBytes()),
            new MockMultipartFile("file", fileNames[1], "application/pdf",
                        fileTextContents[1].getBytes()),
            new MockMultipartFile("file", fileNames[2], "text/plain",
                        fileTextContents[2].getBytes())};

        mockMvc.perform(multipart("/v1/files").file(multipartFiles[0]).file(multipartFiles[1])
                .file(multipartFiles[2]).principal(principal))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$[0].subject", equalTo(fileNames[0])))
                .andExpect(jsonPath("$[0].status", equalTo(HttpStatus.OK.value())))

                .andExpect(jsonPath("$[1].id", nullValue()))
                .andExpect(jsonPath("$[1].subject", equalTo(fileNames[1])))
                .andExpect(jsonPath("$[1].path", nullValue()))
                .andExpect(jsonPath("$[1].status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$[1].message",
                        equalTo(HttpStatus.BAD_REQUEST.name() + ": "
                                + MultipartFileService.BAD_REQUEST_MSG_EXTENSION)))

                .andExpect(jsonPath("$[2].id", nullValue()))
                .andExpect(jsonPath("$[2].subject", equalTo(fileNames[2])))
                .andExpect(jsonPath("$[2].path", nullValue()))
                .andExpect(jsonPath("$[2].status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$[2].message", equalTo(HttpStatus.BAD_REQUEST.name() + ": "
                        + MultipartFileService.BAD_REQUEST_MSG_EMPTY)));

        assertEquals(true, Files.exists(path.resolve(fileNames[0])));
        assertEquals(false, Files.exists(path.resolve(fileNames[1])));
        assertEquals(false, Files.exists(path.resolve(fileNames[2])));

        assertEquals(Files.size(path.resolve(fileNames[0])), fileTextContents[0].getBytes().length);
    }

    @Test
    public void testSendFileNameTooLong() throws Exception {
        char[] chars = new char[252];
        Arrays.fill(chars, 'x');
        String fileName = new String(chars) + ".txt";

        byte[] fileContent = "abcdefghi".getBytes();

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", fileName, "text/plain", fileContent);

        mockMvc.perform(multipart("/v1/files").file(multipartFile).principal(principal))
                .andExpect(status().isMultiStatus()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", nullValue()))
                .andExpect(jsonPath("$[0].subject", equalTo(fileName)))
                .andExpect(jsonPath("$[0].path", nullValue()))
                .andExpect(jsonPath("$[0].status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$[0].message", equalTo(HttpStatus.BAD_REQUEST.name() + ": "
                        + MultipartFileService.BAD_REQUEST_MSG_NAME_LENGTH)));
    }

    private String getExpectedJsonFileList() throws IOException {
        return FileUtils.readFileToString(
                resourceLoader.getResource("classpath:/responses/json/file_list.json").getFile(),
                Charset.forName("UTF-8"));
    }

    @Test
    public void testDeletesSingleFile() throws Exception {

        assertThat(getBspinkFileStatus(5L), not(BsplinkFileStatus.DELETED.toString()));

        mockMvc.perform(delete("/v1/files/5").principal(principal)).andExpect(status().isOk());

        assertThat(getBspinkFileStatus(5L), equalTo(BsplinkFileStatus.DELETED.toString()));
    }

    @Test
    public void testDeleteSingleFileThrowsException() throws Exception {

        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESxx2203_20181010_test15367.txt");
        bsplinkFile.setType("xx");
        bsplinkFile.setBytes(1212L);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        when(yadeUtils.transfer(any(), any(), any(), any())).thenThrow(new Exception());

        mockMvc.perform(delete("/v1/files/" + id).principal(principal)).andExpect(status()
                .isInternalServerError());
    }

    @Test
    public void testDeletesDeletedSingleFile() throws Exception {

        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESxx2203_20181011_test15367.txt");
        bsplinkFile.setType("xx");
        bsplinkFile.setBytes(1212L);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFile.setStatus(BsplinkFileStatus.DELETED);
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        mockMvc.perform(delete("/v1/files/" + id).principal(principal)).andExpect(status()
                .isBadRequest());
    }

    @Test
    public void testDeleteSingleFileReturnsNotFound() throws Exception {

        mockMvc.perform(delete("/v1/files/999").principal(principal)).andExpect(status()
                .isNotFound());
    }

    private String getBspinkFileStatus(Long id) {

        return bsplinkFileRepository.findById(id).get().getStatus().toString();
    }

    @Test
    public void testDeletesMultipleFiles() throws Exception {

        assertThat(getBspinkFileStatus(3L), not(BsplinkFileStatus.DELETED.toString()));
        assertThat(getBspinkFileStatus(4L), not(BsplinkFileStatus.DELETED.toString()));
        assertThat(getBspinkFileStatus(5L), not(BsplinkFileStatus.DELETED.toString()));

        mockMvc.perform(delete("/v1/files?id=3&id=4&id=5").principal(principal))
                .andExpect(status().isMultiStatus());

        assertThat(getBspinkFileStatus(3L), equalTo(BsplinkFileStatus.DELETED.toString()));
        assertThat(getBspinkFileStatus(4L), equalTo(BsplinkFileStatus.DELETED.toString()));
        assertThat(getBspinkFileStatus(5L), equalTo(BsplinkFileStatus.DELETED.toString()));
    }

    @Test
    public void testDeleteMultipleFilesReturnsNotFoundFiles() throws Exception {

        mockMvc.perform(delete("/v1/files?id=5&id=999").principal(principal))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", equalTo(5)))
                .andExpect(jsonPath("$[0].status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$[0].message", equalTo("deleted")))

                .andExpect(jsonPath("$[1].id", equalTo(999)))
                .andExpect(jsonPath("$[1].status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$[1].message", equalTo(HttpStatus.NOT_FOUND.name())));
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
    public void testRegister() throws Exception {
        Long bytes = 88L;
        String name = "abcdefghi.txt";
        BsplinkFileStatus status = BsplinkFileStatus.NOT_DOWNLOADED;
        String type = "abcdefghi.txt";
        Instant instant = Instant.now();

        String json = String.format(
                "{\r\n" + "    \"name\": \"%s\",\r\n" + "    \"type\": \"%s\",\r\n"
                        + "    \"bytes\": %d,\r\n" + "    \"uploadDateTime\": \"%s\",\r\n"
                        + "    \"status\": \"%s\"\r\n" + "}",
                name, type, bytes, instant.toString(), status);

        mockMvc.perform(
                post("/v1/files/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<BsplinkFile> bsplinkFileContainer = bsplinkFileRepository.findByName(name);

        assertEquals(true, bsplinkFileContainer.size() == 1);

        BsplinkFile bsplinkFile = bsplinkFileContainer.get(0);
        assertEquals(bsplinkFile.getBytes(), bytes);
        assertEquals(bsplinkFile.getType(), type);
        assertEquals(bsplinkFile.getStatus(), status);
        assertEquals(bsplinkFile.getUploadDateTime(), instant);
    }


    @Test
    public void testDownloadFileUnauthorized() throws Exception {
        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESna2203_20180910_testt");
        bsplinkFile.setType("na");
        bsplinkFile.setBytes(1L);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                bsplinkFile, FileAccessType.READ, principal.getName()))
                .thenReturn(false);

        mockMvc.perform(get("/v1/files/" + id).principal(principal))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testDownloadFileTrashed() throws Exception {
        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESna2203_20180910_test");
        bsplinkFile.setType("na");
        bsplinkFile.setBytes(1L);
        bsplinkFile.setStatus(BsplinkFileStatus.TRASHED);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                bsplinkFile, FileAccessType.READ, principal.getName()))
                .thenReturn(true);

        mockMvc.perform(get("/v1/files/" + id).principal(principal))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDownloadZipUnauthorized() throws Exception {
        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESna2203_20180910_test");
        bsplinkFile.setType("na");
        bsplinkFile.setBytes(1L);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        when(fileAccessPermissionService.isBsplinkFilesAccessPermittedForUser(
                Arrays.asList(bsplinkFile), FileAccessType.READ, principal.getName()))
                .thenReturn(false);

        mockMvc.perform(get("/v1/files/zip?id=" + id).principal(principal))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testDeletesFileUnauthorized() throws Exception {

        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName("ESxx2203_20181011_test");
        bsplinkFile.setType("xx");
        bsplinkFile.setBytes(1212L);
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFile.setStatus(BsplinkFileStatus.DELETED);
        bsplinkFileRepository.saveAndFlush(bsplinkFile);
        Long id = bsplinkFile.getId();

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                bsplinkFile, FileAccessType.WRITE, principal.getName()))
                .thenReturn(false);

        mockMvc.perform(delete("/v1/files/" + id).principal(principal)).andExpect(status()
                .isUnauthorized());
    }


    @Test
    public void testDeletesFilesListUnauthorized() throws Exception {

        BsplinkFile bsplinkFile1 = new BsplinkFile();
        bsplinkFile1.setName("ESxx2203_20181011_test");
        bsplinkFile1.setType("xx");
        bsplinkFile1.setBytes(1212L);
        bsplinkFile1.setUploadDateTime(Instant.now());
        bsplinkFile1.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        bsplinkFileRepository.saveAndFlush(bsplinkFile1);

        BsplinkFile bsplinkFile2 = new BsplinkFile();
        bsplinkFile2.setName("ESxx2203_20181011_test2");
        bsplinkFile2.setType("xx");
        bsplinkFile2.setBytes(1212L);
        bsplinkFile2.setUploadDateTime(Instant.now());
        bsplinkFile2.setStatus(BsplinkFileStatus.DOWNLOADED);
        bsplinkFileRepository.saveAndFlush(bsplinkFile2);
        bsplinkFile2.getId();

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                bsplinkFile1, FileAccessType.WRITE, principal.getName()))
                .thenReturn(false);
        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(
                bsplinkFile2, FileAccessType.WRITE, principal.getName()))
                .thenReturn(false);

        mockMvc.perform(
                delete("/v1/files?id=" + bsplinkFile1.getId() + "&id=" + bsplinkFile2.getId())
                .principal(principal))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$[0].id", equalTo(bsplinkFile1.getId().intValue())))
                .andExpect(jsonPath("$[0].status", equalTo(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath("$[1].id", equalTo(bsplinkFile2.getId().intValue())))
                .andExpect(jsonPath("$[1].status", equalTo(HttpStatus.UNAUTHORIZED.value())));
    }
}
