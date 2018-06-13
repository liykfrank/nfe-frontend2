package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.test.fixtures.AirlineFixtures.getAirlines;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import junitparams.JUnitParamsRunner;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.service.AirlineService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AirlineValidatorTest extends CustomConstraintValidatorTestCase {

    private final String airlineCodeNotExists = "000001089";
    private final String airlineCodeIncorrectIsoc = "ES00001";

    private AirlineService airlineService;
    private AirlineValidator airlineValidator;

    private AcdmRequest acdmRequest;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        Airline airline = getAirlines().get(0);
        airlineService = mock(AirlineService.class);

        when(airlineService.findAirline(airline.getIsoCountryCode(), airline.getAirlineCode()))
                .thenReturn(airline);
        when(airlineService.findAirline(airlineCodeIncorrectIsoc, airlineCodeNotExists))
                .thenReturn(null);

        acdmRequest = new AcdmRequest();
        acdmRequest.setIsoCountryCode(airline.getIsoCountryCode());
        acdmRequest.setAirlineCode(airline.getAirlineCode());
        acdmRequest.setDateOfIssue(LocalDate.of(2018, 5, 12));

        airlineValidator = new AirlineValidator(airlineService);

    }

    @Test
    public void testIsValid() {
        assertTrue(airlineValidator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsNullAirline() {
        acdmRequest.setAirlineCode(null);
        assertTrue(airlineValidator.isValid(acdmRequest, context));
    }

    @Test
    public void testAirlineIsNotValid() {
        acdmRequest.setAirlineCode(airlineCodeIncorrectIsoc);
        acdmRequest.setIsoCountryCode(airlineCodeIncorrectIsoc);
        assertFalse(airlineValidator.isValid(acdmRequest, context));
        verifyConstraintViolation("airlineCode", AirlineValidator.NOT_FOUND_MSG);
    }

}
