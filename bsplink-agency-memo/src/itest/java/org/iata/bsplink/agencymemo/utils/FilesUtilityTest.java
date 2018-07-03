package org.iata.bsplink.agencymemo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.host.local.protocol=local", "app.host.sftp.protocol=sftp"})
@AutoConfigureMockMvc
public class FilesUtilityTest {

    @Value("${app.local_attached_files_directory}")
    private String localAttachedFilesDirectory;

    @Autowired
    private FilesUtility filesUtility;

    @MockBean
    private YadeUtils yadeUtils;

    @Test
    public void testSaveMultipartFilesFromLocalToRemote() {

        List<MultipartFile> list = new ArrayList<MultipartFile>();

        MockMultipartFile fileOne = new MockMultipartFile("file", "fileOne.txt", "text/plain",
                "fileOne.txt".getBytes());
        list.add(fileOne);

        try {
            filesUtility.saveMultipartFilesFromLocalToRemote(getAcdm(), list);
        } catch (Exception e) {
            fail("Exception is Thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRemotePathForFile() {
        assertEquals("attached_files/ES/acdm/789456123",
                filesUtility.getRemotePathForFile(getAcdm()));
    }

    @Test
    public void testGetRemoteFullPathForFile() {
        assertEquals("attached_files/ES/acdm/789456123/file_one.txt",
                filesUtility.getRemoteFullPathForFile(getAcdm(), getFiles().get(0).getName()));
    }

    @Test
    public void testCreateAttachedDirectory() throws IOException {

        FileUtils.deleteDirectory(new File(Paths.get(localAttachedFilesDirectory).toString()));

        filesUtility.saveFileInTempAttachedDirectory(new MockMultipartFile("file", "fileOne.txt",
                "text/plain", "fileOne.txt".getBytes()));

        File dirAttachedFilesAlreadyCreated =
                new File(Paths.get(localAttachedFilesDirectory).toString());

        assertEquals(true, dirAttachedFilesAlreadyCreated.exists());

    }



    private Acdm getAcdm() {
        Acdm acdm = new Acdm();
        acdm.setId(789456123L);
        acdm.setIsoCountryCode("ES");
        return acdm;
    }

    private List<BsplinkFile> getFiles() {

        BsplinkFile fileOne = new BsplinkFile();
        fileOne.setId(1L);
        fileOne.setName("file_one.txt");
        fileOne.setAcdmId(getAcdm().getId());
        fileOne.setBytes(1000L);
        fileOne.setPath("/path/file_one.txt");
        fileOne.setUploadDateTime(Instant.now());

        List<BsplinkFile> listFiles = new ArrayList<>();
        listFiles.add(fileOne);

        BsplinkFile fileTwo = new BsplinkFile();
        fileTwo.setId(1L);
        fileTwo.setName("file_two.txt");
        fileTwo.setAcdmId(getAcdm().getId());
        fileTwo.setBytes(1000L);
        fileTwo.setPath("/path/file_two.txt");
        fileTwo.setUploadDateTime(Instant.now());
        listFiles.add(fileTwo);

        BsplinkFile fileThree = new BsplinkFile();
        fileThree.setId(1L);
        fileThree.setName("file_three.txt");
        fileThree.setAcdmId(getAcdm().getId());
        fileThree.setBytes(1000L);
        fileThree.setPath("/path/file_three.txt");
        fileThree.setUploadDateTime(Instant.now());
        listFiles.add(fileThree);

        return listFiles;
    }


}
