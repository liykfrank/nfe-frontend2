package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class IssuePermissionValidatorTest {
    private IssuePermissionValidator validator;
    private RefundIssuePermissionService service;
    private Errors errors;
    private Refund refund;
    private String notFoundAirline;

    @Before
    public void setUp() {
        String isoc = "AA";
        String airline = "220";
        String agent = "78200102";

        RefundIssuePermission refundIssuePermission = new RefundIssuePermission();
        refundIssuePermission.setIsoCountryCode(isoc);
        refundIssuePermission.setAirlineCode(airline);
        refundIssuePermission.setAgentCode(agent);

        notFoundAirline = "777";

        refund = new Refund();
        refund.setIsoCountryCode(isoc);
        refund.setAirlineCode(airline);
        refund.setAgentCode(agent);

        service = mock(RefundIssuePermissionService.class);
        when(service.findByIsoCountryCodeAndAirlineCodeAndAgentCode(isoc, airline, agent))
                .thenReturn(Optional.of(refundIssuePermission));
        when(service.findByIsoCountryCodeAndAirlineCodeAndAgentCode(isoc, notFoundAirline, agent))
                .thenReturn(Optional.empty());
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new IssuePermissionValidator(service);
    }

    @Test
    public void testIsValid() {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidAirlineNotFound() {
        refund.setAirlineCode(notFoundAirline);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(IssuePermissionValidator.NO_PERMISSION,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidNullAirline() {
        refund.setAirlineCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullAgent() {
        refund.setAgentCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullCountry() {
        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
