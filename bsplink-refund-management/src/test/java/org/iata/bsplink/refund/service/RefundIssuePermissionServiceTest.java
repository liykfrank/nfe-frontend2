package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.model.repository.RefundIssuePermissionRepository;
import org.junit.Before;
import org.junit.Test;

public class RefundIssuePermissionServiceTest {

    private static final Long PERMISSION_ID = (long) 1;

    private RefundIssuePermission permission;
    private RefundIssuePermissionRepository refundIssuePermissionRepository;
    private RefundIssuePermissionService refundIssuePermissionService;

    @Before
    public void setUp() {
        permission = new RefundIssuePermission();
        refundIssuePermissionRepository = mock(RefundIssuePermissionRepository.class);
        refundIssuePermissionService =
                new RefundIssuePermissionService(refundIssuePermissionRepository);

        permission.setId(PERMISSION_ID);

        when(refundIssuePermissionRepository.save(permission)).thenReturn(permission);
    }

    @Test
    public void testSave() {
        assertThat(refundIssuePermissionService.save(permission), sameInstance(permission));
        verify(refundIssuePermissionRepository).save(permission);
    }

    @Test
    public void testFindAll() {
        List<RefundIssuePermission> permissions = new ArrayList<>();
        when(refundIssuePermissionRepository.findAll()).thenReturn(permissions);
        assertThat(refundIssuePermissionService.findAll(), sameInstance(permissions));
        verify(refundIssuePermissionRepository).findAll();
    }

    @Test
    public void testDelete() {
        refundIssuePermissionService.delete(permission);
        verify(refundIssuePermissionRepository).delete(permission);
    }

    @Test
    public void testFindById() {
        when(refundIssuePermissionRepository.findById(PERMISSION_ID))
                .thenReturn(Optional.of(permission));
        Optional<RefundIssuePermission> optionalRefund =
                refundIssuePermissionService.findById(PERMISSION_ID);
        verify(refundIssuePermissionRepository).findById(PERMISSION_ID);
        assertTrue(optionalRefund.isPresent());
        assertThat(optionalRefund.get(), sameInstance(permission));
    }

    @Test
    public void testFindByIsoCountryCodeAndAirlineCodeAndAgentCode() {
        String airlineCode = "220";
        String agentCode = "78200011";
        String isoCountryCode = "AA";
        when(refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                isoCountryCode, airlineCode, agentCode))
                .thenReturn(Optional.of(permission));
        Optional<RefundIssuePermission> optionalRefund = refundIssuePermissionService
                .findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                        isoCountryCode, airlineCode, agentCode);
        verify(refundIssuePermissionRepository).findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                isoCountryCode, airlineCode, agentCode);
        assertTrue(optionalRefund.isPresent());
        assertThat(optionalRefund.get(), sameInstance(permission));
    }

    @Test
    public void testFindByIsoCountryCodeAndAirlineCode() {
        String airlineCode = "220";
        String isoCountryCode = "AA";
        List<RefundIssuePermission> permissions = new ArrayList<>();
        when(refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCode(
                isoCountryCode, airlineCode))
                .thenReturn(permissions);
        assertThat(refundIssuePermissionService.findByIsoCountryCodeAndAirlineCode(
                isoCountryCode, airlineCode), sameInstance(permissions));
        verify(refundIssuePermissionRepository).findByIsoCountryCodeAndAirlineCode(
                isoCountryCode, airlineCode);
    }


    @Test
    public void testIsPermittedNullBecauseIsoCountryCodeIsNull() {
        Refund refund = new Refund();
        refund.setAgentCode("1");
        refund.setAirlineCode("2");
        assertNull(refundIssuePermissionService.isPermitted(refund));
    }


    @Test
    public void testIsPermittedNullBecauseAirlineCodeIsNull() {
        Refund refund = new Refund();
        refund.setAgentCode("1");
        refund.setIsoCountryCode("X");
        assertNull(refundIssuePermissionService.isPermitted(refund));
    }


    @Test
    public void testIsPermittedNullBecauseAgentCodeIsNull() {
        Refund refund = new Refund();
        refund.setAirlineCode("1");
        refund.setIsoCountryCode("X");
        assertNull(refundIssuePermissionService.isPermitted(refund));
    }


    @Test
    public void testIsPermitted() {
        Refund refund = new Refund();
        refund.setAgentCode("1");
        refund.setAirlineCode("1");
        refund.setIsoCountryCode("X");

        when(refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                refund.getIsoCountryCode(), refund.getAirlineCode(), refund.getAgentCode()))
                .thenReturn(Optional.of(permission));
        assertTrue(refundIssuePermissionService.isPermitted(refund));
    }


    @Test
    public void testIsNotPermitted() {
        Refund refund = new Refund();
        refund.setAgentCode("1");
        refund.setAirlineCode("1");
        refund.setIsoCountryCode("X");

        when(refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                refund.getIsoCountryCode(), refund.getAirlineCode(), refund.getAgentCode()))
                .thenReturn(Optional.empty());
        assertFalse(refundIssuePermissionService.isPermitted(refund));
    }
}
