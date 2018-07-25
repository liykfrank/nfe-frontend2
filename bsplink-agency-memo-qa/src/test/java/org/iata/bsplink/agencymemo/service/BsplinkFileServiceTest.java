package org.iata.bsplink.agencymemo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.iata.bsplink.agencymemo.model.repository.BsplinkFileRepository;
import org.junit.Before;
import org.junit.Test;

public class BsplinkFileServiceTest {

    private BsplinkFile file;
    private BsplinkFileRepository bsplinkFileRepository;
    private BsplinkFileService bsplinkFileService;

    @Before
    public void setUp() {

        file = new BsplinkFile();
        file.setAcdmId(1L);
        file.setBytes(1000L);
        file.setId(1L);
        file.setName("file_one.txt");
        file.setPath("/path");
        file.setUploadDateTime(Instant.now());

        bsplinkFileRepository = mock(BsplinkFileRepository.class);
        bsplinkFileService = new BsplinkFileService(bsplinkFileRepository);
    }


    @Test
    public void testFindById() {

        when(bsplinkFileService.findById(1L)).thenReturn(Optional.of(file));

        BsplinkFile fileReturned = bsplinkFileService.findById(1L).get();

        assertEquals(file, fileReturned);
    }

}
