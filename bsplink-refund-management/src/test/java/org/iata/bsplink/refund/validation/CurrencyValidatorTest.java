package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.iata.bsplink.refund.fake.Currency;
import org.iata.bsplink.refund.fake.IsocCurrencies;
import org.iata.bsplink.refund.model.entity.Refund;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class CurrencyValidatorTest {
    private CurrencyValidator validator;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        String isoc = "AA";
        IsocCurrencies isocCurrencies = new IsocCurrencies();
        Currency currency = isocCurrencies.getCurrenciesByIsoc1(isoc).get(0).getCurrencies().get(0);
        refund = new Refund();
        refund.setIsoCountryCode(isoc);
        refund.getCurrency().setCode(currency.getName());
        refund.getCurrency().setDecimals(currency.getNumDecimals());
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new CurrencyValidator();
    }

    @Test
    public void testIsValid() {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidCurrencyNotFound() {
        refund.getCurrency().setCode("QAZ");
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(CurrencyValidator.NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidNullCurrency() {
        refund.setCurrency(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullCurrencyCode() {
        refund.getCurrency().setCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullCurrencyDecimals() {
        refund.getCurrency().setDecimals(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidCountryNotExists() {
        refund.setIsoCountryCode("XML");
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }


    @Test
    public void testIsValidNullCountry() {
        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullDateOfIssue() {
        refund.setDateOfIssue(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidCurrencyExpired() {
        refund.setDateOfIssue(LocalDate.of(9000, 2, 2));
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(CurrencyValidator.EXPIRED_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
