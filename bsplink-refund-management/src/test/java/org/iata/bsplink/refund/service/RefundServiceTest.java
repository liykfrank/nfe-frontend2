package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefunds;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.exception.ActionNotAllowedForRefundStatus;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.model.repository.RefundRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RefundServiceTest {

    private Refund refund;
    private Refund refundWithId;
    private RefundService refundService;
    private RefundRepository refundRepository;
    private RefundHistoryService refundHistoryService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        refund = getRefunds().get(0);
        refundWithId = getRefunds().get(2);
        refundRepository = mock(RefundRepository.class);
        refundHistoryService = mock(RefundHistoryService.class);
        refundService = new RefundService(refundRepository, refundHistoryService);
        when(refundRepository.findById(refundWithId.getId())).thenReturn(Optional.of(refundWithId));
    }

    @Test
    public void testSave() {
        when(refundRepository.save(any())).thenReturn(refundWithId);
        Refund savedRefund = refundService.saveIndirectRefund(refund);
        assertThat(savedRefund, equalTo(refundWithId));
        verify(refundRepository).save(refund);
    }

    @Test
    public void testUpadetRefundOk() {
        when(refundRepository.save(any())).thenReturn(refundWithId);
        Refund updatedRefund = refundService.updateIndirectRefund(refundWithId);
        assertNotNull(updatedRefund);
        assertThat(updatedRefund, equalTo(refundWithId));
        verify(refundRepository).save(refundWithId);
    }

    @Test
    public void testFindById() {
        when(refundRepository.findById(1L)).thenReturn(Optional.of(refund));
        Optional<Refund> found = refundService.findById(1L);
        assertTrue(found.isPresent());
        assertThat(found.get(), sameInstance(refund));
    }

    @Test
    public void testFindAll() {
        List<Refund> refunds = getRefunds();
        when(refundRepository.findAll()).thenReturn(refunds);
        List<Refund> findAll = refundService.findAll();
        assertThat(findAll, sameInstance(refunds));
    }

    @Test
    public void testDeletePending() throws Exception {
        refund.setStatus(RefundStatus.PENDING);
        refundService.deleteIndirectRefund(refund);
        verify(refundRepository).delete(refund);
    }

    @Test
    public void testDeleteDraft() throws Exception {
        refund.setStatus(RefundStatus.DRAFT);
        refundService.deleteIndirectRefund(refund);
        verify(refundRepository).delete(refund);
    }

    @Test(expected = ActionNotAllowedForRefundStatus.class)
    public void testDeleteAuthorized() throws Exception {

        refund.setStatus(RefundStatus.AUTHORIZED);
        refundService.deleteIndirectRefund(refund);
    }

    @Test(expected = ActionNotAllowedForRefundStatus.class)
    public void testDeleteRejected() throws Exception {
        refund.setStatus(RefundStatus.REJECTED);
        refundService.deleteIndirectRefund(refund);
    }

    @Test
    public void testDoesNotDeletesRefundWhenActionNotAllowedForRefundStatusIsThrown() {

        refund.setStatus(RefundStatus.AUTHORIZED);

        try {
            refundService.deleteIndirectRefund(refund);
        } catch (ActionNotAllowedForRefundStatus e) {
            // do nothing
        }

        verify(refundRepository, never()).delete(refund);
    }


    @Test
    public void testFindByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber() {
        Optional<Refund> found;
        found = refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                refundWithId.getIsoCountryCode(),
                refundWithId.getAirlineCode(),
                refundWithId.getTicketDocumentNumber());
        assertTrue(found.isPresent());
        assertThat(found.get(), sameInstance(refundWithId));
    }

    @Test
    public void testFindByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumberNotFound() {
        Optional<Refund> found;
        found = refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                refundWithId.getIsoCountryCode(),
                refundWithId.getAirlineCode(),
                "1234567890");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumberNotCorrect() {
        Optional<Refund> found;
        found = refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                refundWithId.getIsoCountryCode(),
                refundWithId.getAirlineCode(),
                "ABCDEFGHIJKLM");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByIsoCountryCodeAndOtherAirlineCodeAndTicketDocumentNumber() {
        Optional<Refund> found;
        found = refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                refundWithId.getIsoCountryCode(),
                "951",
                refundWithId.getTicketDocumentNumber());
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByOtherIsoCountryCodeAndOtherAirlineCodeAndTicketDocumentNumber() {
        Optional<Refund> found;
        found = refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                "KQ",
                refundWithId.getAirlineCode(),
                refundWithId.getTicketDocumentNumber());
        assertFalse(found.isPresent());
    }

    @Test
    public void testUpadetRefundWithFileName() {
        String fileName = "filename.txt";
        when(refundRepository.save(any())).thenReturn(refundWithId);
        Refund updatedRefund = refundService.updateIndirectRefund(refundWithId, fileName);
        assertNotNull(updatedRefund);
        assertThat(updatedRefund, equalTo(refundWithId));
        verify(refundRepository).save(refundWithId);
    }
}
