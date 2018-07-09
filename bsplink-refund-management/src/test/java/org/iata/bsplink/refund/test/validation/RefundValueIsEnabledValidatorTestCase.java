package org.iata.bsplink.refund.test.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.iata.bsplink.refund.validation.RefundValueIsEnabledValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@RunWith(JUnitParamsRunner.class)
public abstract class RefundValueIsEnabledValidatorTestCase<T> {

    private static final String ANY_ISO_COUNTRY_CODE = "ES";

    protected Refund refund;
    protected RefundValueIsEnabledValidator validator;
    protected Config config;
    protected ConfigService configService;
    protected Errors errors;

    protected abstract void setValue(Refund refund, T value);

    protected abstract void enableValue(Config config, boolean enable);

    protected abstract Object[][] validationCases();

    protected abstract T getDefinedValue();

    protected abstract RefundValueIsEnabledValidator getValidator(ConfigService configService);

    @Before
    public void setUp() {

        refund = new Refund();
        refund.setIsoCountryCode(ANY_ISO_COUNTRY_CODE);

        config = new Config();
        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        errors = new BeanPropertyBindingResult(refund, "refund");

        validator = getValidator(configService);
    }

    @Test
    public void testSupports() {

        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testResultIsValidIfDoesntHaveIsoCountryCode() {

        setValue(refund, getDefinedValue());
        enableValue(config, false);

        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());

        refund.setIsoCountryCode("");
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @Parameters(method = "validationCases")
    public void testValidatesObject(T value, boolean enabled, boolean hasErrors) {

        setValue(refund, value);
        enableValue(config, enabled);

        validator.validate(refund, errors);

        assertEquals(hasErrors, errors.hasErrors());
    }

    @Test
    public void testAddExpectedMessageWhenNotValid() {

        setValue(refund, getDefinedValue());
        enableValue(config, false);

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());

        FieldError fieldError = errors.getFieldError();

        assertEquals(validator.getFieldName(), fieldError.getField());
        assertEquals(validator.getDefaultMessage(), fieldError.getDefaultMessage());
    }

}
