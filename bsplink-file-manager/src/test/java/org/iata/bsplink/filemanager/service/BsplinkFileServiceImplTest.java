package org.iata.bsplink.filemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.response.EntityActionResponse;
import org.iata.bsplink.filemanager.utils.BsplinkFileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class BsplinkFileServiceImplTest {

    private BsplinkFileRepository bsplinkFileRepository;
    private BsplinkFileService bsplinkFileService;
    private BsplinkFileUtils bsplinkFileUtils;
    private FileAccessPermissionService fileAccessPermissionService;
    private String user;

    @Before
    public void setUp() {
        user = "USER";
        bsplinkFileRepository = mock(BsplinkFileRepository.class);
        bsplinkFileUtils = mock(BsplinkFileUtils.class);
        fileAccessPermissionService = mock(FileAccessPermissionService.class);

        when(fileAccessPermissionService.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(any()))
                .thenReturn(true);
        bsplinkFileService = new BsplinkFileServiceImpl(
                bsplinkFileRepository, bsplinkFileUtils, fileAccessPermissionService);
    }

    @Test
    public void testUpdateStatusToTrashed() {
        BsplinkFile file = getBspLinkFileMock().get();
        when(bsplinkFileRepository.findByName(file.getName()))
                .thenReturn(getList());
        List<BsplinkFile> fileUpdates = bsplinkFileService.updateStatusToTrashed(file);
        assertEquals(BsplinkFileStatus.TRASHED, fileUpdates.get(0).getStatus());
    }

    @Test
    public void testDeleteMultipleFilesIsAwareOfDeleteExceptions() {

        setUpDeleteMultipleFilesTestMocks();

        List<Long> ids = Arrays.asList(1L, 2L);

        when(fileAccessPermissionService.isBsplinkFileAccessPermittedForUser(any(), any(), any()))
            .thenReturn(true);
        List<EntityActionResponse<Long>> result = bsplinkFileService.deleteMultipleFiles(ids, user);

        for (EntityActionResponse<Long> response : result) {
            assertThat(response.getStatus(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            assertThat(response.getMessage(), equalTo("Unable to delete file"));
        }
    }

    private void setUpDeleteMultipleFilesTestMocks() {

        when(bsplinkFileRepository.findById(any())).thenReturn(Optional.of(new BsplinkFile()))
                .thenReturn(Optional.of(new BsplinkFile()));
        when(bsplinkFileRepository.save(any())).thenThrow(new RuntimeException("foo"));
    }

    @Test
    public void testDeleteDeletedMultipleFilesReturnsBadRequest() {

        setUpDeleteDeletedFilesTestMocks();

        List<Long> ids = Arrays.asList(1L, 2L);

        List<EntityActionResponse<Long>> result = bsplinkFileService.deleteMultipleFiles(ids, user);

        for (EntityActionResponse<Long> response : result) {
            assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
        }
    }

    private void setUpDeleteDeletedFilesTestMocks() {
        BsplinkFile bsplinkFile1 = new BsplinkFile();
        bsplinkFile1.setStatus(BsplinkFileStatus.DELETED);
        BsplinkFile bsplinkFile2 = new BsplinkFile();
        bsplinkFile2.setStatus(BsplinkFileStatus.DELETED);

        when(bsplinkFileRepository.findById(any())).thenReturn(Optional.of(bsplinkFile1))
                .thenReturn(Optional.of(bsplinkFile2));;
    }

    @Test
    public void testFindById() {

        when(bsplinkFileRepository.findById(1L)).thenReturn(getBspLinkFileMock());

        Optional<BsplinkFile> bsplinkFile = bsplinkFileService.findById(1L);

        assertThat(bsplinkFile.get()).isEqualToComparingFieldByField(getBspLinkFileMock().get());
    }


    @Test
    public void testFindByIdNotFound() {

        when(bsplinkFileRepository.findById(any())).thenReturn(Optional.of(new BsplinkFile()));

        assertThat(bsplinkFileService.findById(1L).get())
                .isEqualToComparingFieldByField(getVoidBspLinkFileMock().get());
    }


    @Test
    public void testSaveBsplinkFile() {

        when(bsplinkFileRepository.save(any()))
                .thenReturn(getBsplinkFileMockDownloadStatus().get());

        assertThat(bsplinkFileService.save(getBspLinkFileMock().get()))
                .isEqualToComparingFieldByField(getBsplinkFileMockDownloadStatus().get());

    }


    @Test
    public void testUpdateStatusToDownloaded() {

        when(bsplinkFileRepository.save(any()))
                .thenReturn(getBsplinkFileMockDownloadStatus().get());

        assertThat(bsplinkFileService.updateStatusToDownloaded(getBspLinkFileMock().get()))
                .isEqualToComparingFieldByField(getBsplinkFileMockDownloadStatus().get());

    }

    private Optional<BsplinkFile> getBspLinkFileMock() {

        Optional<BsplinkFile> optionalFile = Optional.of(new BsplinkFile());

        optionalFile.get().setId(1L);
        optionalFile.get().setName("file1");
        optionalFile.get().setStatus(BsplinkFileStatus.NOT_DOWNLOADED);
        optionalFile.get().setType("type");
        optionalFile.get().setUploadDateTime(Instant.parse("2018-04-04T12:00:00Z"));
        optionalFile.get().setBytes(1024L);

        return optionalFile;
    }

    private List<BsplinkFile> getList() {
        List<BsplinkFile> list = new ArrayList<>();
        list.add(getBspLinkFileMock().get());
        return list;
    }

    private Optional<BsplinkFile> getBsplinkFileMockDownloadStatus() {

        Optional<BsplinkFile> bsplinkFile = getBspLinkFileMock();
        bsplinkFile.get().setStatus(BsplinkFileStatus.DOWNLOADED);

        return bsplinkFile;
    }

    private Optional<BsplinkFile> getVoidBspLinkFileMock() {

        return Optional.of(new BsplinkFile());
    }

}
