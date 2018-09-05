package org.iata.bsplink.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkOptionRepository;
import org.iata.bsplink.user.service.BsplinkOptionService;
import org.junit.Before;
import org.junit.Test;

public class BsplinkOptionServiceTest {

    private BsplinkOptionRepository repository;

    private BsplinkOptionService service;


    @Before
    public void setUp() {
        repository = mock(BsplinkOptionRepository.class);
        service = new BsplinkOptionService(repository);
    }

    @Test
    public void testFindById() {

        BsplinkOption option = new BsplinkOption();
        option.setId("OPTION");
        when(repository.findById(option.getId())).thenReturn(Optional.of(option));
        Optional<BsplinkOption> optionFound = service.findById(option.getId());
        verify(repository).findById(option.getId());
        assertTrue(optionFound.isPresent());
        assertThat(optionFound.get(), equalTo(option));
    }


    @Test
    public void testFindAll() {

        List<BsplinkOption> options = new ArrayList<>();
        when(repository.findAll()).thenReturn(options);
        List<BsplinkOption> optionsFound = service.findAll();
        verify(repository).findAll();
        assertThat(optionsFound, equalTo(options));
    }


    @Test
    public void testFindByUserType() {

        List<BsplinkOption> options = new ArrayList<>();
        when(repository.findByUserTypes(UserType.AGENT)).thenReturn(options);
        List<BsplinkOption> optionsFound = service.findByUserType(UserType.AGENT);
        verify(repository).findByUserTypes(UserType.AGENT);
        assertThat(optionsFound, equalTo(options));
    }

    @Test
    public void testFindByUserTypeNotExists() {

        List<BsplinkOption> options = new ArrayList<>();
        when(repository.findByUserTypes(UserType.AGENT)).thenReturn(options);
        List<BsplinkOption> optionsFound = service.findByUserType(UserType.AIRLINE);
        verify(repository).findByUserTypes(UserType.AIRLINE);
        assertThat(optionsFound, IsEmptyCollection.empty());
    }
}
