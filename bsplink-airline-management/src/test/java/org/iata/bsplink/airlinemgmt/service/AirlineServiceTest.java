package org.iata.bsplink.airlinemgmt.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.repository.AirlineRepository;
import org.junit.Before;
import org.junit.Test;

public class AirlineServiceTest {

    private AirlineService airlineService;
    private AirlineRepository airlineRepository;

    @Before
    public void setUp() {

        airlineRepository = mock(AirlineRepository.class);
        airlineService = new AirlineService(airlineRepository);
    }

    @Test
    public void testSaveAll() {

        List<Airline> airlines = new ArrayList<>();
        when(airlineRepository.saveAll(airlines)).then(returnsFirstArg());

        List<Airline> savedAirlines = airlineService.saveAll(airlines);

        assertThat(savedAirlines, sameInstance(airlines));
        verify(airlineRepository).saveAll(airlines);
    }

    @Test
    public void testFindAll() {

        List<Airline> airlines = new ArrayList<>();
        when(airlineRepository.findAll()).thenReturn(airlines);

        List<Airline> recoveredAirlines = airlineService.findAll();

        assertThat(recoveredAirlines, sameInstance(airlines));
    }

    @Test
    public void testFindByAirlineCodeAndIsoCountryCode() {

        Optional<Airline> optionalAirline = Optional.empty();

        String airlineCode = "001";
        String isoCountryCode = "ES";

        when(airlineRepository.findByAirlinePkAirlineCodeAndAirlinePkIsoCountryCode(airlineCode,
                isoCountryCode)).thenReturn(optionalAirline);

        Optional<Airline> retrievedOptionalAirline =
                airlineService.findByAirlineCodeAndIsoCountryCode(airlineCode, isoCountryCode);

        assertThat(retrievedOptionalAirline, sameInstance(optionalAirline));
    }

    @Test
    public void testDelete() {

        Airline airline = new Airline();

        airlineService.delete(airline);

        verify(airlineRepository).delete(airline);
    }

}
