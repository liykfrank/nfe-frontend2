package org.iata.bsplink.filemanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.repository.FileAccessPermissionRepository;
import org.junit.Before;
import org.junit.Test;

public class FileAccessPermissionServiceTest {


    FileAccessPermissionRepository repository;
    FileAccessPermissionService service;

    @Before
    public void setUp() {
        repository = mock(FileAccessPermissionRepository.class);
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
