package org.iata.bsplink.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
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
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkTemplateRepository;
import org.iata.bsplink.user.model.repository.UserTemplateRepository;
import org.junit.Before;
import org.junit.Test;

public class BsplinkTemplateServiceTest {

    private BsplinkTemplateRepository repository;

    private BsplinkTemplateService service;

    private UserTemplateRepository userRepository;


    @Before
    public void setUp() {

        repository = mock(BsplinkTemplateRepository.class);
        userRepository = mock(UserTemplateRepository.class);
        service = new BsplinkTemplateService(repository, userRepository);
    }

    @Test
    public void testFindById() {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TEMPLATE");
        when(repository.findById(template.getId())).thenReturn(Optional.of(template));
        Optional<BsplinkTemplate> templateFound = service.findById(template.getId());
        verify(repository).findById(template.getId());
        assertTrue(templateFound.isPresent());
        assertThat(templateFound.get(), equalTo(template));
    }


    @Test
    public void testFindAll() {

        List<BsplinkTemplate> templates = new ArrayList<>();
        when(repository.findAll()).thenReturn(templates);
        List<BsplinkTemplate> templatesFound = service.findAll();
        verify(repository).findAll();
        assertThat(templatesFound, equalTo(templates));
    }


    @Test
    public void testFindByUserType() {

        List<BsplinkTemplate> templates = new ArrayList<>();
        when(repository.findByUserTypes(UserType.AGENT)).thenReturn(templates);
        List<BsplinkTemplate> templatesFound = service.findByUserType(UserType.AGENT);
        verify(repository).findByUserTypes(UserType.AGENT);
        assertThat(templatesFound, equalTo(templates));
    }

    @Test
    public void testFindByUserTypeNotExists() {

        List<BsplinkTemplate> templates = new ArrayList<>();
        when(repository.findByUserTypes(UserType.AGENT)).thenReturn(templates);
        List<BsplinkTemplate> templatesFound = service.findByUserType(UserType.AIRLINE);
        verify(repository).findByUserTypes(UserType.AIRLINE);
        assertThat(templatesFound, IsEmptyCollection.empty());
    }


    @Test
    public void testSave() {

        BsplinkTemplate template = new BsplinkTemplate();
        when(repository.save(template)).thenReturn(template);
        BsplinkTemplate templateSaved = service.save(template);
        verify(repository).save(template);
        assertThat(templateSaved, equalTo(template));
    }


    @Test
    public void testDelete() {

        BsplinkTemplate template = new BsplinkTemplate();
        service.delete(template);
        verify(repository).delete(template);
    }

    @Test
    public void testDeleteOption() {
        BsplinkOption option = new BsplinkOption();
        BsplinkTemplate template = new BsplinkTemplate();
        template.getOptions().add(option);
        service.deleteOption(template, option);
        verify(repository).save(template);
        assertThat(template.getOptions(), not(hasItem(option)));
    }
}