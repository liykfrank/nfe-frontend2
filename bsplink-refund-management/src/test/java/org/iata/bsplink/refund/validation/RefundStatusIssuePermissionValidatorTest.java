package org.iata.bsplink.refund.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefunds;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

public class RefundStatusIssuePermissionValidatorTest {

    private RefundStatusIssuePermissionValidator validator;
    private Refund refund;
    private BeanPropertyBindingResult errors;

    private RefundIssuePermissionService permissionService;

    @Before
    public void setUp() {
        refund = getRefunds().get(0);
        errors = new BeanPropertyBindingResult(refund, "refund");
        permissionService = mock(RefundIssuePermissionService.class);
        validator = new RefundStatusIssuePermissionValidator(permissionService);
    }


    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }


    @Test
    public void testIsValid() {
        when(permissionService.isPermitted(refund)).thenReturn(Boolean.TRUE);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }


    @Test
    public void testIsNotValid() {
        when(permissionService.isPermitted(refund)).thenReturn(Boolean.FALSE);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getField(), equalTo("airlineCode"));
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(RefundStatusIssuePermissionValidator.NO_PERMISSION));
    }
}
