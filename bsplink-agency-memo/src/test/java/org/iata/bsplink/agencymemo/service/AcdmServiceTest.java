package org.iata.bsplink.agencymemo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.agencymemo.test.fixtures.AcdmFixtures.getAcdms;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.RelatedTicketDocumentRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.model.repository.AcdmRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

public class AcdmServiceTest {

    private AcdmRepository acdmRepository;
    private AcdmService acdmService;
    private ConfigService configService;

    @Before
    public void setUp() {
        acdmRepository = mock(AcdmRepository.class);
        configService = mock(ConfigService.class);
        when(configService.find("X")).thenReturn(new Config());
        acdmService = new AcdmService(acdmRepository, configService);
    }


    @Test
    public void testSave() {
        Acdm acdm = getAcdms().get(0);
        AcdmRequest acdmRequest = new AcdmRequest();
        copyPropertiesFromAcdmToAcdmRequest(acdm, acdmRequest);

        when(acdmRepository.save(any())).thenReturn(acdm);
        Acdm savedAcdm = acdmService.save(acdmRequest);

        assertThat(savedAcdm, equalTo(acdm));
        verify(acdmRepository).save(acdm);
    }


    @Test
    public void testCopyPropertiesFromAcdmRequestToAcdm() {
        Acdm acdm1 = getAcdms().get(0);
        AcdmRequest acdmRequest = new AcdmRequest();

        copyPropertiesFromAcdmToAcdmRequest(acdm1, acdmRequest);

        Acdm acdm2 = new Acdm();
        acdmService.copyPropertiesFromAcdmRequestToAcdm(acdmRequest, acdm2);

        assertThat(acdm2, equalTo(acdm1));
    }

    private void copyPropertiesFromAcdmToAcdmRequest(Acdm acdm, AcdmRequest acdmRequest) {
        BeanUtils.copyProperties(acdm, acdmRequest);
        BeanUtils.copyProperties(acdm.getAgentCalculations(), acdmRequest.getAgentCalculations());
        BeanUtils.copyProperties(acdm.getAirlineCalculations(),
                acdmRequest.getAirlineCalculations());
        BeanUtils.copyProperties(acdm.getAirlineContact(), acdmRequest.getAirlineContact());
        BeanUtils.copyProperties(acdm.getCurrency(), acdmRequest.getCurrency());

        acdmRequest.setRelatedTicketDocuments(
                acdm.getRelatedTicketDocuments().stream().map(r -> {
                    RelatedTicketDocumentRequest req = new RelatedTicketDocumentRequest();
                    BeanUtils.copyProperties(r, req);
                    return req;
                }).collect(Collectors.toList()));

        acdmRequest.setTaxMiscellaneousFees(acdm.getTaxMiscellaneousFees().stream().map(t -> {
            TaxMiscellaneousFeeRequest req = new TaxMiscellaneousFeeRequest();
            BeanUtils.copyProperties(t, req);
            return req;
        }).collect(Collectors.toList()));
    }


    @Test
    public void testFindAll() throws Exception {
        List<Acdm> acdms = getAcdms();
        when(acdmRepository.findAll()).thenReturn(acdms);
        List<Acdm> findAll = acdmService.findAll();
        assertThat(findAll, sameInstance(acdms));
    }


    @Test
    public void testRegularization() throws Exception {
        AcdmRequest acdm = new AcdmRequest();
        acdm.setIsoCountryCode("X");
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(101));
        acdm.getAgentCalculations().setTax(BigDecimal.valueOf(103));
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(303));
        acdm.getAirlineCalculations().setTax(BigDecimal.valueOf(97));
        acdm.setRegularized(null);

        acdmService.regularization(acdm);
        assertNotNull(acdm.getRegularized());
        assertTrue(acdm.getRegularized());
    }

    @Test
    public void testRegularizationFalse() throws Exception {
        AcdmRequest acdm = new AcdmRequest();
        acdm.setIsoCountryCode("X");
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(101));
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(303));
        acdm.setRegularized(null);

        acdmService.regularization(acdm);
        assertNotNull(acdm.getRegularized());
        assertFalse(acdm.getRegularized());
    }
}
