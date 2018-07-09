package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Reason;
import org.iata.bsplink.refund.model.entity.ReasonType;
import org.iata.bsplink.refund.model.repository.ReasonRepository;
import org.junit.Before;
import org.junit.Test;

public class ReasonServiceTest {

    private static final Long REASON_ID = (long) 1;

    private Reason reason;
    private ReasonRepository reasonRepository;
    private ReasonService reasonService;

    @Before
    public void setUp() {

        reason = new Reason();
        reasonRepository = mock(ReasonRepository.class);
        reasonService = new ReasonService(reasonRepository);

        reason.setId(REASON_ID);

        when(reasonRepository.save(reason)).thenReturn(reason);
    }

    @Test
    public void testSave() {

        assertThat(reasonService.save(reason), sameInstance(reason));
        verify(reasonRepository).save(reason);
    }

    @Test
    public void testFindAll() {

        List<Reason> reasons = new ArrayList<>();
        when(reasonRepository.findAll()).thenReturn(reasons);

        assertThat(reasonService.findAll(), sameInstance(reasons));
        verify(reasonRepository).findAll();
    }

    @Test
    public void testFindByIsoCountryCode() {
        String isoc = "ES";
        List<Reason> reasons = new ArrayList<>();
        when(reasonRepository.findByIsoCountryCode(isoc)).thenReturn(reasons);

        assertThat(reasonService.findByIsoCountryCode(isoc), sameInstance(reasons));
        verify(reasonRepository).findByIsoCountryCode(isoc);
    }

    @Test
    public void testFindByIsoCountryCodeAndType() {
        String isoc = "ES";
        ReasonType type = ReasonType.REASON_FOR_ISSUANCE;
        List<Reason> reasons = new ArrayList<>();
        when(reasonRepository.findByIsoCountryCodeAndType(isoc, type)).thenReturn(reasons);

        assertThat(reasonService.findByIsoCountryCodeAndType(isoc, type), sameInstance(reasons));
        verify(reasonRepository).findByIsoCountryCodeAndType(isoc, type);
    }

    @Test
    public void testDelete() {

        reasonService.delete(reason);

        verify(reasonRepository).delete(reason);
    }

    @Test
    public void testUpdatesReason() {

        when(reasonRepository.existsById(REASON_ID)).thenReturn(true);

        Optional<Reason> optionalReason = reasonService.update(reason);

        verify(reasonRepository).save(reason);

        assertTrue(optionalReason.isPresent());
        assertThat(optionalReason.get(), sameInstance(reason));
    }

    @Test
    public void testIsAwareOfInexistentReasonOnUpdate() {

        when(reasonRepository.existsById(REASON_ID)).thenReturn(false);

        Optional<Reason> optionalReason = reasonService.update(reason);

        verify(reasonRepository, never()).save(reason);

        assertFalse(optionalReason.isPresent());
    }

}