package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AirlineService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class AirlineValidatorTest {
    private AirlineValidator validator;
    private AirlineService airlineService;
    private Errors errors;
    private Refund refund;
    private Airline airline;
    private String airlineNotFound = "666";

    @Before
    public void setUp() {
        airline = new Airline();
        airline.setAirlineCode("220");
        airline.setIsoCountryCode("ES");
        refund = new Refund();
        refund.setIsoCountryCode(airline.getIsoCountryCode());
        refund.setAirlineCode(airline.getAirlineCode());
        airlineService = mock(AirlineService.class);
        when(airlineService.findAirline(refund.getIsoCountryCode(), refund.getAirlineCode()))
                .thenReturn(airline);
        when(airlineService.findAirline(refund.getIsoCountryCode(), airlineNotFound))
                .thenReturn(null);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new AirlineValidator(airlineService);
    }

    @Test
    public void testIsValid() {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidAirlineNotFound() {
        refund.setAirlineCode(airlineNotFound);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(AirlineValidator.NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidNullAirline() {
        refund.setAirlineCode(null);
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
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
