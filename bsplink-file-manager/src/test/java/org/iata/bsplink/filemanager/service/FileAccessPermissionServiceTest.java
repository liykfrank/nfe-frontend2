package org.iata.bsplink.filemanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
import org.iata.bsplink.filemanager.model.repository.FileAccessPermissionRepository;
import org.junit.Before;
import org.junit.Test;

public class FileAccessPermissionServiceTest {

    private FileAccessPermissionRepository repository;
    private FileAccessPermissionService service;

    private String fileNameAuthorized;
    private String fileNameUnauthorized;

    @Before
    public void setUp() {
        repository = mock(FileAccessPermissionRepository.class);

        when(repository.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                "user", "ES", "ab", FileAccessType.READ)).thenReturn(true);

        when(repository.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                "user", "ES", "xy", FileAccessType.READ)).thenReturn(false);

        fileNameAuthorized = "ESab2203_123456";
        fileNameUnauthorized = "ESxy2203_123456";

        service = new FileAccessPermissionService(repository);
    }

    @Test
    public void testCreate() {
        FileAccessPermission fap = new FileAccessPermission();
        when(repository.save(fap)).thenReturn(fap);
        FileAccessPermission fapCreated = service.create(fap);

        assertThat(fapCreated, equalTo(fap));
        verify(repository).save(fap);
    }


    @Test
    public void testExistsByUserAndIsoCountryCodeAndFileTypeAndAccess() {

        FileAccessPermission fap = new FileAccessPermission();

        when(repository.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                fap.getUser(),
                fap.getIsoCountryCode(),
                fap.getFileType(),
                fap.getAccess())).thenReturn(true);

        boolean exists = service.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(fap);

        assertTrue(exists);
        verify(repository).existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                fap.getUser(),
                fap.getIsoCountryCode(),
                fap.getFileType(),
                fap.getAccess());
    }


    @Test
    public void testIsFileAccessNotPermittedForUserWithFileNameNull() {
        assertFalse(service.isFileAccessPermittedForUser(null, FileAccessType.READ, "user"));
    }


    @Test
    public void testIsFileAccessNotPermittedForUserWithTooShortFileName() {
        assertFalse(service.isFileAccessPermittedForUser("123", FileAccessType.READ, "user"));
    }


    @Test
    public void testIsFileAccessNotPermittedForUserWithUserNull() {
        assertFalse(service.isFileAccessPermittedForUser(fileNameAuthorized,
                FileAccessType.READ, null));
    }


    @Test
    public void testIsFileAccessPermitted() {
        assertTrue(service.isFileAccessPermittedForUser(fileNameAuthorized,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testIsFileAccessNotPermitted() {
        assertFalse(service.isFileAccessPermittedForUser(fileNameUnauthorized,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testIsBsplinkFileAccessPermittedForUser() {
        BsplinkFile file = new BsplinkFile();
        file.setName(fileNameAuthorized);
        assertTrue(service.isBsplinkFileAccessPermittedForUser(file,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testIsBsplinkFileAccessNotPermittedForUser() {
        BsplinkFile file = new BsplinkFile();
        file.setName(fileNameUnauthorized);
        assertFalse(service.isBsplinkFileAccessPermittedForUser(file,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testIsBsplinkFilesAccessPermittedForUser() {
        BsplinkFile file = new BsplinkFile();
        file.setName(fileNameAuthorized);

        List<BsplinkFile> files = Arrays.asList(file);
        assertTrue(service.isBsplinkFilesAccessPermittedForUser(files,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testIsBsplinkFilesAccessNotPermittedForUser() {
        BsplinkFile file1 = new BsplinkFile();
        file1.setName(fileNameAuthorized);
        BsplinkFile file2 = new BsplinkFile();
        file2.setName(fileNameUnauthorized);

        List<BsplinkFile> files = Arrays.asList(file1, file2);
        assertFalse(service.isBsplinkFilesAccessPermittedForUser(files,
                FileAccessType.READ, "user"));
    }


    @Test
    public void testNotExistsByUserAndIsoCountryCodeAndFileTypeAndAccess() {

        FileAccessPermission fap = new FileAccessPermission();

        when(repository.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                fap.getUser(),
                fap.getIsoCountryCode(),
                fap.getFileType(),
                fap.getAccess())).thenReturn(false);

        boolean exists = service.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(fap);

        assertFalse(exists);
        verify(repository).existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                fap.getUser(),
                fap.getIsoCountryCode(),
                fap.getFileType(),
                fap.getAccess());
    }


    @Test
    public void testFindAll() {

        List<FileAccessPermission> faps = new ArrayList<>();
        when(repository.findAll()).thenReturn(faps);
        List<FileAccessPermission> fapsFound = service.findAll();
        assertThat(fapsFound, equalTo(faps));
        verify(repository).findAll();
    }


    @Test
    public void testFindByUser() {

        String user = "user";
        List<FileAccessPermission> faps = new ArrayList<>();
        when(repository.findByUser(user)).thenReturn(faps);
        List<FileAccessPermission> fapsFound = service.findByUser(user);
        assertThat(fapsFound, equalTo(faps));
        verify(repository).findByUser(user);
    }


    @Test
    public void testDelete() {

        FileAccessPermission fap = new FileAccessPermission();
        service.delete(fap);
        verify(repository).delete(fap);
    }
}
