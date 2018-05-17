package org.iata.bsplink.filemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.response.EntityActionResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class BsplinkFileServiceImplTest {

    private BsplinkFileRepository bsplinkFileRepository;
    private BsplinkFileService bsplinkFileService;   

    @Before
    public void setUp() {

        bsplinkFileRepository = mock(BsplinkFileRepository.class);
        bsplinkFileService = new BsplinkFileServiceImpl(bsplinkFileRepository);
    }

    @Test
    public void testDeleteMultipleFilesIsAwareOfDeleteExceptions() {

        setUpDeleteMultipleFilesTestMocks();

        List<Long> ids = Arrays.asList(1L, 2L);

        List<EntityActionResponse<Long>> result = bsplinkFileService.deleteMultipleFiles(ids);

        for (EntityActionResponse<Long> response : result) {
            assertThat(response.getStatus(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            assertThat(response.getMessage(), equalTo("Unable to delete file"));
        }
    }

    private void setUpDeleteMultipleFilesTestMocks() {

        Optional<BsplinkFile> optionalFile = Optional.of(new BsplinkFile());

        when(bsplinkFileRepository.findById(any())).thenReturn(optionalFile);
        when(bsplinkFileRepository.save(any())).thenThrow(new RuntimeException("foo"));
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
        optionalFile.get().setStatus(BsplinkFileStatus.UNREAD);
        optionalFile.get().setType("type");
        optionalFile.get().setUploadDateTime(Instant.parse("2018-04-04T12:00:00Z"));
        optionalFile.get().setBytes(1024L);

        return optionalFile;
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
