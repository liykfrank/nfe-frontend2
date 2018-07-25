package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.iata.bsplink.agencymemo.fake.Country;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class CountryValidatorTest extends CustomConstraintValidatorTestCase {

    private CountryValidator validator;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        validator = new CountryValidator();
    }

    @Test
    public void testIsValid() {
        String isocExists = Country.findAllCountries().get(0).getIsoCountryCode();
        assertTrue(validator.isValid(isocExists, context));
    }

    @Test
    public void testIsNullValid() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    public void testIsNotValid() {
        assertFalse(validator.isValid("XXX", context));
        verifyConstraintViolation(CountryValidator.NOT_FOUND_MSG);
    }
}
