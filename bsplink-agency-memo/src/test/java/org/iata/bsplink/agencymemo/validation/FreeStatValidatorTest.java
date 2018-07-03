package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FreeStatValidatorTest {

    private FreeStatValidator validator;
    private Config config;
    private ConfigService configService;
    private Errors errors;
    private AcdmRequest acdm;

    @Before
    public void setUp() {
        acdm = new AcdmRequest();
        acdm.setIsoCountryCode("AA");
        config = new Config();
        config.setFreeStat(false);
        configService = mock(ConfigService.class);
        when(configService.find(acdm.getIsoCountryCode())).thenReturn(config);
        errors = new BeanPropertyBindingResult(acdm, "acdmRequest");
        validator = new FreeStatValidator(configService);
    }

    @Test
    public void testIsValidFreeStatIsTrue() {
        config.setFreeStat(true);
        acdm.setStatisticalCode("IBC");
        validator.validate(acdm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidFreeStatIsFalse() {
        acdm.setStatisticalCode("I");
        validator.validate(acdm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValid() {
        acdm.setStatisticalCode("IBC");
        validator.validate(acdm, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(FreeStatValidator.INCORRECT_VALUE_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidIsoCountryCodeIsNull() {
        acdm.setIsoCountryCode(null);
        acdm.setStatisticalCode("IBC");
        validator.validate(acdm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidStatisticalCodeIsNull() {
        acdm.setStatisticalCode(null);
        validator.validate(acdm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(acdm.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
