package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.iata.bsplink.refund.fake.Country;
import org.iata.bsplink.refund.model.entity.Refund;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class CountryValidatorTest {
    private CountryValidator validator;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = new Refund();
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new CountryValidator();
    }

    @Test
    public void testIsValid() {
        String isoc = Country.findAllCountries().get(0).getIsoCountryCode();
        refund.setIsoCountryCode(isoc);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNullValid() {
        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValid() {
        refund.setIsoCountryCode("XXX");
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(CountryValidator.NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
