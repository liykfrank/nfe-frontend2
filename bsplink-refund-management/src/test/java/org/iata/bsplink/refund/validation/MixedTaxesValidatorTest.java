package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@RunWith(JUnitParamsRunner.class)
public class MixedTaxesValidatorTest {

    private MixedTaxesValidator validator;
    private Config config;
    private ConfigService configService;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = new Refund();
        refund.setIsoCountryCode("AA");
        config = new Config();
        configService = mock(ConfigService.class);
        when(configService.find(refund.getIsoCountryCode())).thenReturn(config);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new MixedTaxesValidator(configService);
    }

    @Test
    public void testIsValidWithoutTaxes() {
        config.setMixedTaxesAllowed(false);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    @Parameters
    public void testIsValid(Boolean mixTax, Integer tax,
            FormOfPaymentAmount... formOfPaymentAmount) {
        config.setMixedTaxesAllowed(mixTax);
        if (tax == null) {
            refund.getAmounts().setTax(null);
        } else {
            refund.getAmounts().setTax(BigDecimal.valueOf(tax));
        }
        refund.setFormOfPaymentAmounts(Arrays.asList(formOfPaymentAmount));
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    @Parameters
    public void testIsNotValid(Boolean mixTax, Integer tax,
            FormOfPaymentAmount... formOfPaymentAmount) {
        config.setMixedTaxesAllowed(mixTax);
        refund.getAmounts().setTax(BigDecimal.valueOf(tax));
        refund.setFormOfPaymentAmounts(Arrays.asList(formOfPaymentAmount));
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(MixedTaxesValidator.DEFAULT_MESSAGE,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    @Parameters
    public void testIsValidIfIsNullField(String field) throws Exception {
        BeanUtils.setProperty(refund, field, null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }


    private FormOfPaymentAmount fop(FormOfPaymentType type, Integer amount) {
        FormOfPaymentAmount fop = new FormOfPaymentAmount();
        fop.setType(type);
        if (amount != null) {
            fop.setAmount(BigDecimal.valueOf(amount));
        }
        return fop;
    }

    /**
     * Parameters for Is Valid test.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { false, 10, fop(FormOfPaymentType.CA, 7) },
            { false, 10, fop(FormOfPaymentType.CC, 7) },
            { false, 10, fop(FormOfPaymentType.CA, 17), fop(FormOfPaymentType.CC, 7) },
            { false, 10, fop(FormOfPaymentType.CA, 7), fop(FormOfPaymentType.MSCA, 7),
                fop(FormOfPaymentType.CC, 7) },
            { true, 10, fop(FormOfPaymentType.CA, 7), fop(FormOfPaymentType.CC, 7) },
            { true, 10, fop(FormOfPaymentType.CA, 7) },
            { true, 10, fop(FormOfPaymentType.CC, 7) },
            { true, 10, fop(FormOfPaymentType.CA, 17), fop(FormOfPaymentType.CC, 7) },
            { false, null, fop(FormOfPaymentType.MSCA, 17), fop(FormOfPaymentType.CC, 7) },
            { false, 21, fop(FormOfPaymentType.MSCA, 17), fop(null, 1),
                    fop(FormOfPaymentType.CC, 7) },
            { false, 21, fop(FormOfPaymentType.MSCA, 17), fop(FormOfPaymentType.CA, null),
                    fop(FormOfPaymentType.CC, 7) }
        };
    }

    /**
     * Parameters for Is Not Valid test.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { false, 5, fop(FormOfPaymentType.CA, 4), fop(FormOfPaymentType.MSCC, 7) },
            { false, 21, fop(FormOfPaymentType.MSCA, 17), fop(FormOfPaymentType.CA, 3),
                fop(FormOfPaymentType.CC, 7) }
        };
    }

    /**
     * Returns fields which with null value validate okay.
     */
    public Object[][] parametersForTestIsValidIfIsNullField() {
        return new Object[][] {
            { "isoCountryCode" },
            { "amounts" },
            { "formOfPaymentAmounts" }
        };
    }
}
