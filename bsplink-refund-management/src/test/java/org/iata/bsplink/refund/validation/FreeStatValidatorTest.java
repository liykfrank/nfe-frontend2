package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FreeStatValidatorTest {

    private FreeStatValidator validator;
    private Config config;
    private ConfigService configService;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = new Refund();
        refund.setIsoCountryCode("AA");
        config = new Config();
        config.setFreeStat(false);
        configService = mock(ConfigService.class);
        when(configService.find(refund.getIsoCountryCode())).thenReturn(config);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new FreeStatValidator(configService);
    }

    @Test
    public void testIsValidFreeStatIsTrue() {
        config.setFreeStat(true);
        refund.setStatisticalCode("IBC");
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidFreeStatIsFalse() {
        refund.setStatisticalCode("I");
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValid() {
        refund.setStatisticalCode("IBC");
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(FreeStatValidator.INCORRECT_VALUE_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidIsoCountryCodeIsNull() {
        refund.setIsoCountryCode(null);
        refund.setStatisticalCode("IBC");
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidStatisticalCodeIsNull() {
        refund.setStatisticalCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
