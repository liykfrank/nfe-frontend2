package org.iata.bsplink.agencymemo.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class AcdmValueIsEnabledValidatorTestCase
        extends CustomConstraintValidatorTestCase {

    private static final String ANY_ISO_COUNTRY_CODE = "ES";
    private static final String ANY_VALUE = "1";

    private AcdmRequest acdmRequest;
    private AcdmValueIsEnabledValidator validator;
    private Config config;
    private ConfigService configService;

    protected abstract void setValue(AcdmRequest acdm, String value);

    protected abstract void enableValue(Config config, boolean enable);

    protected abstract String getFieldName();

    protected abstract AcdmValueIsEnabledValidator getValidator(ConfigService configService);

    protected abstract String getContraintViolationMessage();

    @Override
    @Before
    public void setUp() {

        super.setUp();

        acdmRequest = new AcdmRequest();
        acdmRequest.setIsoCountryCode(ANY_ISO_COUNTRY_CODE);

        config = new Config();
        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        validator = getValidator(configService);
    }

    @Test
    public void testReturnsValidIfAcdmDoesntHaveIsoCountryCode() {

        setValue(acdmRequest, ANY_VALUE);
        enableValue(config, false);

        acdmRequest.setIsoCountryCode(null);
        assertTrue(validator.isValid(acdmRequest, context));

        acdmRequest.setIsoCountryCode("");
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    @Parameters
    public void testIsValid(String registrationNumber, boolean registrationNumberEnabled,
            boolean expected) {

        setValue(acdmRequest, registrationNumber);
        enableValue(config, registrationNumberEnabled);

        assertThat(validator.isValid(acdmRequest, context), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestIsValid() {

        return new Object[][] {
            { ANY_VALUE, true, true },
            { null, true, true },
            { "", true, true },
            { ANY_VALUE, false, false },
        };
    }

    @Test
    public void testAddExpectedMessageWhenNotValid() {

        setValue(acdmRequest, ANY_VALUE);
        enableValue(config, false);

        assertFalse(validator.isValid(acdmRequest, context));

        verifyConstraintViolation(getFieldName(), getContraintViolationMessage());
    }

}
