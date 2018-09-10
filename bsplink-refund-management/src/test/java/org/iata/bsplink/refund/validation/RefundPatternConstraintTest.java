package org.iata.bsplink.refund.validation;

import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefunds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.refund.model.entity.Refund;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(JUnitParamsRunner.class)
public class RefundPatternConstraintTest {
    private LocalValidatorFactoryBean validator;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = getRefunds().get(0);
        errors = new BeanPropertyBindingResult(refund, Refund.class.getSimpleName());
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    @Parameters
    public void testIsValid(String field, String value) throws Exception {
        BeanUtils.setProperty(refund, field, value);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    @Parameters
    public void testIsNotValid(String field, String value) throws Exception {
        BeanUtils.setProperty(refund, field, value);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(ValidationMessages.INCORRECT_FORMAT,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Parameters for Is Valid.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { "passenger", "PEPE" },
            { "passenger", "DR. MED. A-B" },
            { "waiverCode", "AB12/Z.0-" },
            { "tourCode", "ABC" },
            { "settlementAuthorisationCode", "ABC d12" },
            { "statisticalCode", "INT" },
            { "statisticalCode", "DOM" },
            { "statisticalCode", "I" },
            { "statisticalCode", "D" },
            { "statisticalCode", "" }
        };
    }

    /**
     * Parameters for Is Not Valid.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { "passenger", "X" + (char)(Integer.parseInt("40", 16)) },
            { "passenger", "X" + (char)(Integer.parseInt("5B", 16)) },
            { "passenger", "paszenger" },
            { "waiverCode", "Ñ" },
            { "waiverCode", "AAa" },
            { "waiverCode", "zZZ" },
            { "tourCode", "X" + (char)(Integer.parseInt("22", 16)) },
            { "tourCode", "X" + (char)(Integer.parseInt("7F", 16)) },
            { "settlementAuthorisationCode", "ABC,d12" },
            { "customerFileReference", "ABC,d12" },
            { "statisticalCode", "X" }
        };
    }
}
