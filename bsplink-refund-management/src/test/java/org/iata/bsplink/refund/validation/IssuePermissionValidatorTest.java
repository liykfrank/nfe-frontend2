package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.model.entity.Refund;
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

    @Before
    public void setUp() {
        refund = new Refund();

        service = mock(RefundIssuePermissionService.class);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new IssuePermissionValidator(service);
    }

    @Test
    public void testIsValid() {
        when(service.isPermitted(refund)).thenReturn(Boolean.TRUE);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidAirlineNotFound() {
        when(service.isPermitted(refund)).thenReturn(Boolean.FALSE);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(IssuePermissionValidator.NO_PERMISSION,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidNull() {
        when(service.isPermitted(refund)).thenReturn(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
