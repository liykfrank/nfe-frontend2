package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.test.fixtures.AcdmFixtures.getAcdms;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.iata.bsplink.agencymemo.dto.AcdmCurrencyRequest;
import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class CurrencyValidatorTest extends CustomConstraintValidatorTestCase {

    private CurrencyValidator validator;

    private AcdmRequest acdmRequest;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        Acdm acdm = getAcdms().get(0);

        acdmRequest = new AcdmRequest();
        acdmRequest.setIsoCountryCode(acdm.getIsoCountryCode());
        acdmRequest.setDateOfIssue(LocalDate.of(2018, 5, 12));

        AcdmCurrencyRequest acdmCurrency = new AcdmCurrencyRequest();
        acdmRequest.setCurrency(acdmCurrency);

        acdmCurrency.setCode(acdm.getCurrency().getCode());
        acdmCurrency.setDecimals(acdm.getCurrency().getDecimals());

        validator = new CurrencyValidator();
    }

    @Test
    public void testIsValid() {
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsValidNullIsoCountryCode() {
        acdmRequest.setIsoCountryCode(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsValidNullCurrency() {
        acdmRequest.setCurrency(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsValidNullCurrencyCode() {
        acdmRequest.getCurrency().setCode(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsValidNullCurrencyDecimals() {
        acdmRequest.getCurrency().setDecimals(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsNotValidCodeNotfound() {
        acdmRequest.getCurrency().setCode("XSW");
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("currency", CurrencyValidator.NOT_FOUND_MSG);
    }

    @Test
    public void testIsNotValidDecimalsNotfound() {
        acdmRequest.getCurrency().setDecimals(9);
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("currency", CurrencyValidator.NOT_FOUND_MSG);
    }

    @Test
    public void testIsNotValidCurrencyExpired() {
        acdmRequest.setDateOfIssue(LocalDate.of(3000, 12, 31));
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("currency", CurrencyValidator.EXPIRED_MSG);
    }

    @Test
    public void testIsValidNoDateOfIssue() {
        acdmRequest.setDateOfIssue(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }
}
