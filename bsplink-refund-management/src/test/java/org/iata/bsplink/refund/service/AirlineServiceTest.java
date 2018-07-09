package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.iata.bsplink.refund.test.fixtures.AirlineFixtures.getAirlines;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.exception.FeignClientException;
import org.iata.bsplink.refund.restclient.AirlineClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AirlineServiceTest {

    private AirlineService airlineService;
    private Airline airline;
    private ResponseEntity<Airline> respAirline;
    private String testIsoc;
    private String notFoundAirlineCode;
    private String errorAirlineCode;

    @Before
    public void setUp() {
        airline = getAirlines().get(0);
        testIsoc = "XX";
        notFoundAirlineCode = "XXX";
        errorAirlineCode = "ERROR";

        AirlineClient airlineClient = mock(AirlineClient.class);

        respAirline = ResponseEntity.status(HttpStatus.OK).body(airline);
        when(airlineClient.findAirline(airline.getIsoCountryCode(), airline.getAirlineCode()))
            .thenReturn(respAirline);

        ResponseEntity<Airline> notFound = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(airlineClient.findAirline(testIsoc, notFoundAirlineCode)).thenReturn(notFound);

        ResponseEntity<Airline> badRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        when(airlineClient.findAirline(testIsoc, errorAirlineCode)).thenReturn(badRequest);

        airlineService = new AirlineService(airlineClient);
    }


    @Test
    public void testFindAirlineResponse() throws Exception {
        ResponseEntity<Airline> respAirlineFound =
                airlineService.findAirlineResponse(airline.getIsoCountryCode(),
                        airline.getAirlineCode());
        assertThat(respAirlineFound, is(notNullValue()));
        assertThat(respAirlineFound.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(respAirlineFound.getBody(), is(notNullValue()));
        assertThat(respAirlineFound.getBody(), equalTo(airline));
    }

    @Test
    public void testFindAirlineResponseNotFound() throws Exception {
        ResponseEntity<Airline> respAirlineFound =
                airlineService.findAirlineResponse(testIsoc, notFoundAirlineCode);
        assertThat(respAirlineFound, is(notNullValue()));
        assertThat(respAirlineFound.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testFindAirline() throws Exception {
        Airline airlineFound =
                airlineService.findAirline(airline.getIsoCountryCode(), airline.getAirlineCode());
        assertThat(airlineFound, is(notNullValue()));
        assertThat(airlineFound, equalTo(airline));
    }

    @Test
    public void testFindAirlineNotFound() throws Exception {
        Airline airlineFound = airlineService.findAirline(testIsoc, notFoundAirlineCode);
        assertThat(airlineFound, is(nullValue()));
    }

    @Test (expected = FeignClientException.class)
    public void testFindAirlineBadRequest() throws Exception {
        airlineService.findAirline(testIsoc, errorAirlineCode);
    }
}
