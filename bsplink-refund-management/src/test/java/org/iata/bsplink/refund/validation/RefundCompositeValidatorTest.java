package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.iata.bsplink.refund.model.entity.Refund;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RefundCompositeValidatorTest {

    private Validator validator1;
    private Validator validator2;
    private RefundCompositeValidator refundValidator;

    @Before
    public void setUp() {

        validator1 = mock(Validator.class);
        validator2 = mock(Validator.class);
        refundValidator = new RefundCompositeValidator(Arrays.asList(validator1, validator2));
    }

    @Test
    public void testIsAwareOfSupportedClasses() {

        when(validator2.supports(Refund.class)).thenReturn(true);

        assertTrue(refundValidator.supports(Refund.class));
    }

    @Test
    public void testIsAwareOfUnsupportedClasses() {

        assertFalse(refundValidator.supports(Refund.class));
    }

    @Test
    public void testInvokesValidationOnValidatorsWhichSupportTarget() {

        when(validator2.supports(Refund.class)).thenReturn(true);

        Refund refund = new Refund();
        Errors errors = mock(Errors.class);

        refundValidator.validate(refund, errors);

        verify(validator1, never()).validate(refund, errors);
        verify(validator2).validate(refund, errors);
    }

}
