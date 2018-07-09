package org.iata.bsplink.refund.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.BsplinkFile;
import org.iata.bsplink.refund.model.repository.BsplinkFileRepository;
import org.iata.bsplink.refund.service.BsplinkFileService;
import org.junit.Before;
import org.junit.Test;

public class BsplinkFileServiceTest {

    private BsplinkFile file;
    private List<BsplinkFile> listFiles;
    private BsplinkFileRepository bsplinkFileRepository;
    private BsplinkFileService bsplinkFileService;

    @Before
    public void setUp() {

        file = new BsplinkFile();
        file.setRefundId(1L);
        file.setBytes(1000L);
        file.setId(1L);
        file.setName("file_one.txt");
        file.setPath("/path");
        file.setUploadDateTime(Instant.now());

        listFiles = new ArrayList<>();
        listFiles.add(file);

        bsplinkFileRepository = mock(BsplinkFileRepository.class);
        bsplinkFileService = new BsplinkFileService(bsplinkFileRepository);
    }


    @Test
    public void testFindById() {

        when(bsplinkFileService.findById(1L)).thenReturn(Optional.of(file));

        BsplinkFile fileReturned = bsplinkFileService.findById(1L).get();

        assertEquals(file, fileReturned);
    }

    @Test
    public void testFindByRefundId() {

        when(bsplinkFileService.findByRefundId(1L)).thenReturn(listFiles);

        List<BsplinkFile> listFilesReturned = bsplinkFileService.findByRefundId(1L);

        assertEquals(listFiles, listFilesReturned);
    }

    @Test
    public void testSaveFile() {

        when(bsplinkFileService.save(file)).thenReturn(file);
        
        BsplinkFile fileSaved = bsplinkFileService.save(file);
        
        assertEquals(file, fileSaved);

    }
}
