package org.iata.bsplink.filemanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.iata.bsplink.filemanager.configuration.ApplicationConfiguration;
import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
import org.iata.bsplink.filemanager.response.SimpleResponse;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("/fixtures/sql/user_files.sql")
public class MultipartFileServiceTest {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private MultipartFileService multipartFileService;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigurationService;

    @MockBean
    private FileAccessPermissionService fileAccessPermissionService;

    @MockBean
    private YadeUtils yadeUtils;

    private String user;

    @Before
    public void setUp() throws IOException, BsplinkValidationException {

        user = "USER";
        when(fileAccessPermissionService.isFileAccessPermittedForUser(any(), any(), any()))
                .thenReturn(true);

        createUploadFolder();
        updateBsplinkFileBasicConfig();
    }

    @After
    public void tearDown() throws IOException {
        File uploadFolder = new File(applicationConfiguration.getLocalUploadedFilesDirectory());
        FileUtils.forceDelete(uploadFolder);
    }

    @Test
    public void testSendMultiple() throws Exception {

        String[] fileNames = {"bsplink_file_controller_test1.txt",
            "BSPlink_file_controller_test2.pdf", "bsplink_file_controller_test3.txt"};

        String[] fileTextContents = {"abcdefghi", "XXX", ""};

        List<MultipartFile> multipartFiles = Arrays.asList(
                new MockMultipartFile("file", fileNames[0], "text/plain",
                        fileTextContents[0].getBytes()),
                new MockMultipartFile("file", fileNames[1], "application/pdf",
                        fileTextContents[1].getBytes()),
                new MockMultipartFile("file", fileNames[2], "text/plain",
                        fileTextContents[2].getBytes()));

        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(3, simpleResponses.size());

        assertEquals(simpleResponses.get(0).getSubject(), fileNames[0]);
        assertEquals(simpleResponses.get(0).getStatus(), Integer.valueOf(HttpStatus.OK.value()));
        assertEquals(simpleResponses.get(0).getMessage(), HttpStatus.OK.name());

        assertNull(simpleResponses.get(1).getId());
        assertEquals(simpleResponses.get(1).getSubject(), fileNames[1]);
        assertNull(simpleResponses.get(1).getPath());
        assertEquals(simpleResponses.get(1).getStatus(),
                Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
        assertEquals(simpleResponses.get(1).getMessage(), HttpStatus.BAD_REQUEST.name() + ": "
                + MultipartFileService.BAD_REQUEST_MSG_PATTERN);

        assertNull(simpleResponses.get(2).getId());
        assertEquals(simpleResponses.get(2).getSubject(), fileNames[2]);
        assertNull(simpleResponses.get(2).getPath());
        assertEquals(simpleResponses.get(2).getStatus(),
                Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
        assertEquals(simpleResponses.get(2).getMessage(),
                HttpStatus.BAD_REQUEST.name() + ": " + MultipartFileService.BAD_REQUEST_MSG_EMPTY);

        Path path = Paths.get(applicationConfiguration.getLocalUploadedFilesDirectory());

        assertEquals(true, Files.exists(path.resolve(fileNames[0])));
        assertEquals(false, Files.exists(path.resolve(fileNames[1])));
        assertEquals(false, Files.exists(path.resolve(fileNames[2])));

        assertEquals(Files.size(path.resolve(fileNames[0])), fileTextContents[0].getBytes().length);

        List<SimpleResponse> simpleResponsesAlreadySaved =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(Integer.valueOf(HttpStatus.OK.value()),
                simpleResponsesAlreadySaved.get(0).getStatus());
    }


    @Test
    public void testSendMultipleUnauthorized() throws Exception {

        String fileName = "abcdef";

        when(fileAccessPermissionService.isFileAccessPermittedForUser(
                fileName, FileAccessType.WRITE, user)).thenReturn(false);

        List<MultipartFile> multipartFiles = Arrays.asList(
                new MockMultipartFile("file", fileName, "text/plain",
                        "abcdefghi".getBytes()));

        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(1, simpleResponses.size());
        assertEquals(fileName, simpleResponses.get(0).getSubject());
        assertEquals(Integer.valueOf(HttpStatus.UNAUTHORIZED.value()),
                simpleResponses.get(0).getStatus());
    }


    @Test
    public void testSendMultipleUnallowedFileExtension() throws Exception {

        String fileName = "abcdef.gil";

        List<MultipartFile> multipartFiles = Arrays.asList(
                new MockMultipartFile("file", fileName, "text/plain",
                        "abc".getBytes()));

        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(1, simpleResponses.size());
        assertEquals(fileName, simpleResponses.get(0).getSubject());
        assertEquals(Integer.valueOf(HttpStatus.BAD_REQUEST.value()),
                simpleResponses.get(0).getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.name() + ": "
                + MultipartFileService.BAD_REQUEST_MSG_EXTENSION,
                simpleResponses.get(0).getMessage());
    }


    @Test
    public void testSendFileNameTooLong() throws Exception {
        char[] chars = new char[252];
        Arrays.fill(chars, 'x');
        String fileName = new String(chars) + ".txt";

        byte[] fileContent = "abcdefghi".getBytes();

        List<MultipartFile> multipartFiles =
                Arrays.asList(new MockMultipartFile("file", fileName, "text/plain", fileContent));

        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(1, simpleResponses.size());

        assertNull(simpleResponses.get(0).getId());
        assertEquals(simpleResponses.get(0).getSubject(), fileName);
        assertNull(simpleResponses.get(0).getPath());
        assertEquals(simpleResponses.get(0).getStatus(),
                Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
        assertEquals(simpleResponses.get(0).getMessage(), HttpStatus.BAD_REQUEST.name() + ": "
                + MultipartFileService.BAD_REQUEST_MSG_NAME_LENGTH);
    }

    @Test
    public void testSendNumberExceeded() {

        int numFiles = 2 + bsplinkFileConfigurationService.find().getMaxUploadFilesNumber();
        final byte[] fileContent = "x".getBytes();

        List<MultipartFile> multipartFiles = IntStream.range(1, numFiles).mapToObj(
            i -> new MockMultipartFile("file", "file" + i + ".txt", "text/plain", fileContent))
            .collect(Collectors.toList());

        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(multipartFiles, user);

        assertEquals(1, simpleResponses.size());
        assertNull(simpleResponses.get(0).getId());

        assertEquals(simpleResponses.get(0).getStatus(),
                Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
        assertEquals(simpleResponses.get(0).getMessage(),
                HttpStatus.BAD_REQUEST.name() + ": " + MultipartFileService.BAD_REQUEST_MSG_COUNT);
    }

    @Test
    public void testSendNumberWithoutLimitControll() throws BsplinkValidationException {
        BsplinkFileBasicConfig cfg = bsplinkFileConfigurationService.find();
        cfg.setMaxUploadFilesNumber(-1);
        bsplinkFileConfigurationService.update(cfg);

        int numFiles = 2 + bsplinkFileConfigurationService.find().getMaxUploadFilesNumber();
        final byte[] fileContent = "x".getBytes();

        List<MultipartFile> multipartFile = IntStream.range(1, numFiles).mapToObj(
            i -> new MockMultipartFile("file", "file" + i + ".txt", "text/plain", fileContent))
            .collect(Collectors.toList());

        List<SimpleResponse> responses = multipartFileService.saveFiles(multipartFile, user);
        assertEquals(responses.size(), multipartFile.size());

        assertTrue(responses.stream().allMatch(r -> r.getStatus().equals(HttpStatus.OK.value())));
    }

    @Test
    public void testSendWithIoException() throws IOException {
        MultipartFile multipartFile = mock(MockMultipartFile.class);
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getName()).thenReturn("file");
        when(multipartFile.getOriginalFilename()).thenReturn("abc.txt");
        when(multipartFile.getSize()).thenReturn(5L);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenThrow(new IOException());
        when(multipartFile.getBytes()).thenThrow(new IOException());
        List<SimpleResponse> simpleResponses =
                multipartFileService.saveFiles(Arrays.asList(multipartFile), user);

        assertEquals(1, simpleResponses.size());

        SimpleResponse simpleResponse = simpleResponses.get(0);

        assertNull(simpleResponse.getId());
        assertNull(simpleResponse.getPath());
        assertEquals(simpleResponse.getStatus(),
                Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        assertEquals(simpleResponse.getSubject(), multipartFile.getOriginalFilename());
    }

    private void updateBsplinkFileBasicConfig() throws BsplinkValidationException {
        BsplinkFileBasicConfig cfg = bsplinkFileConfigurationService.find();
        cfg.setAllowedFileExtensions(Arrays.asList("txt", "pdf", ""));
        cfg.setFileNamePatterns(Arrays.asList("[a-z0-9_.]*"));
        cfg.setMaxUploadFilesNumber(3);
        bsplinkFileConfigurationService.update(cfg);
    }

    private void createUploadFolder() throws IOException {
        File uploadFolder = new File(applicationConfiguration.getLocalUploadedFilesDirectory());
        if (uploadFolder.exists()) {
            FileUtils.cleanDirectory(uploadFolder);
        } else {
            uploadFolder.mkdir();
        }
    }
}
