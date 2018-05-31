package org.iata.bsplink.filemanager.utils;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.yadeutils.YadeUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.yade.host.local.directory:files"})
@ActiveProfiles("test")
public class BsplinkFileUtilsTest {

    @Autowired
    private BsplinkFileUtils bsplinkFileUtils;

    @MockBean
    private YadeUtils yadeUtils;

    @Value("${app.local_downloaded_files_directory}")
    private String localDownloadedFilesDirectory;

    private Path uploadedFilesDirectory;

    private static File dirUploadedFiles;

    @Before
    public void setUp() throws IOException {

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

    @Test
    public void testGenerateZipFile() throws Exception {

        HttpServletResponse response = mock(HttpServletResponse.class);

        String[] files = dirUploadedFiles.list();
        String[] filesToDownload =
            {getFilesSftp().get(0).getName(), getFilesSftp().get(1).getName()};

        assertArrayEquals(files, filesToDownload);

        bsplinkFileUtils.generateZipFileInResponse(response, getFilesSftp());

        String[] filesVoid = dirUploadedFiles.list();

        assertThat(filesVoid.length, is(0));
    }

    @Test
    public void testYadeGetFileFromRemoteToLocal() throws Exception {

        bsplinkFileUtils.yadeGetFileFromRemoteHost(getFilesSftp().get(1));

        String contextPathToHostDirectory = Thread.currentThread().getContextClassLoader()
                .getResource(bsplinkFileUtils.getYadeHostLocalDirectory()).getPath();

        File file = new File(
                contextPathToHostDirectory + File.separator + getFilesSftp().get(0).getName());

        assertNotNull(file);
        assertThat(file.getName(), containsString(getFilesSftp().get(0).getName()));

    }

    @Test
    public void testUploadSingleFileFromLocalToRemote() throws Exception {

        when(yadeUtils.transfer(any(), any(), any(), any())).thenReturn(true);

        boolean result =
                bsplinkFileUtils.uploadSingleFileFromLocalToRemote("ILei8385_20180128_file1");

        assertEquals(true, result);

    }

    private List<BsplinkFile> getFilesSftp() {

        BsplinkFile file1 = new BsplinkFile();
        file1.setId(1L);
        file1.setName("ILei8385_20180128_file1");
        file1.setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
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


        List<BsplinkFile> listbsplinkFiles = new ArrayList<>();
        listbsplinkFiles.add(file1);
        listbsplinkFiles.add(file2);

        return listbsplinkFiles;
    }

}
