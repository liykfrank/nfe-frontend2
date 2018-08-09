package org.iata.bsplink.agencymemo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.agencymemo.test.fixtures.TaxOnCommissionTypeFixtures.getTaxOnCommissionTypes;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.repository.TaxOnCommissionTypeRepository;
import org.junit.Before;
import org.junit.Test;

public class TaxOnCommissionTypeServiceTest {

    private TaxOnCommissionTypeRepository taxOnCommissionTypeRepository;
    private TaxOnCommissionTypeService taxOnCommissionTypeService;

    @Before
    public void setUp() {
        taxOnCommissionTypeRepository = mock(TaxOnCommissionTypeRepository.class);
        taxOnCommissionTypeService = new TaxOnCommissionTypeService(taxOnCommissionTypeRepository);
    }

    @Test
    public void testSave() {
        TaxOnCommissionType tctp = getTaxOnCommissionTypes().get(0);
        when(taxOnCommissionTypeRepository.save(any())).thenReturn(tctp);
        TaxOnCommissionType savedTctp = taxOnCommissionTypeService.save(tctp);
        verify(taxOnCommissionTypeRepository).save(tctp);
        assertThat(savedTctp, equalTo(tctp));
    }

    @Test
    public void testFindByIsoCountryCode() throws Exception {
        String isoc = getTaxOnCommissionTypes().get(0).getPk().getIsoCountryCode();
        List<TaxOnCommissionType> tctps = getTaxOnCommissionTypes().stream()
                .filter(tctp -> isoc.equals(tctp.getPk().getIsoCountryCode()))
                .collect(Collectors.toList());
        when(taxOnCommissionTypeRepository.findByPkIsoCountryCode(isoc)).thenReturn(tctps);
        List<TaxOnCommissionType> found = taxOnCommissionTypeService.findByIsoCountryCode(isoc);
        verify(taxOnCommissionTypeRepository).findByPkIsoCountryCode(isoc);
        assertThat(found, sameInstance(tctps));

    }

    @Test
    public void testFind() throws Exception {
        TaxOnCommissionType tctp = getTaxOnCommissionTypes().get(0);
        when(taxOnCommissionTypeRepository.findById(tctp.getPk())).thenReturn(Optional.of(tctp));
        Optional<TaxOnCommissionType> found = taxOnCommissionTypeService.find(tctp.getPk());
        assertTrue(found.isPresent());
        assertThat(found.get(), sameInstance(tctp));
    }

    @Test
    public void testFindDelete() throws Exception {
        TaxOnCommissionType tctp = getTaxOnCommissionTypes().get(0);
        taxOnCommissionTypeService.delete(tctp);
        verify(taxOnCommissionTypeRepository).delete(tctp);
    }
}

