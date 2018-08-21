package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.agencymemo.test.fixtures.AirlineFixtures.getAirlines;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.service.AirlineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AirlineControllerTest {

    private static final String URI_FORMAT = "/v1/airlines/%s/%s";
    private static final String NON_EXISTENT_AIRLINE_URI = "/v1/airlines/XX/XXX";

    private Airline airline;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AirlineService airlineService;
    
    @Autowired
    protected WebApplicationContext webAppContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
        airline = getAirlines().get(0);

        ResponseEntity<Airline> respAirline = ResponseEntity.status(HttpStatus.OK).body(airline);
        when(airlineService
                .findAirlineResponse(airline.getIsoCountryCode(), airline.getAirlineCode()))
                .thenReturn(respAirline);

        ResponseEntity<Airline> notFound = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(airlineService.findAirlineResponse("XX", "XXX")).thenReturn(notFound);
    }

    @Test
    public void testGetAirline() throws Exception {
        String responseBody = mockMvc.perform(
            get(String.format(URI_FORMAT, airline.getIsoCountryCode(), airline.getAirlineCode()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Airline actual = mapper.readValue(responseBody, Airline.class);

        assertThat(actual, equalTo(airline));
    }

    @Test
    public void testGetAirlineNotFound() throws Exception {
        mockMvc.perform(get(NON_EXISTENT_AIRLINE_URI)).andExpect(status().isNotFound());
    }

}
