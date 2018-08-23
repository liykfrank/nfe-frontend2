package org.iata.bsplink.airlinemgmt.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.AIRLINE_1_CODE;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.AIRLINE_1_COUNTRY;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getAirlinesFixture;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.entity.AirlinePk;
import org.iata.bsplink.airlinemgmt.model.repository.AirlineRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AirlineControllerTest {

    private static final String BASE_URI = "/v1/airlines";
    private static final String AIRLINE_1_URI =
            String.format(BASE_URI + "/%s/%s", AIRLINE_1_COUNTRY, AIRLINE_1_CODE);
    private static final String NON_EXISTENT_AIRLINE_URI = BASE_URI + "/AA/001";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webAppContext;

    private List<Airline> airlines;
    private String airlinesJson;
    private AirlinePk airline1Pk;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
        airlines = getAirlinesFixture();
        airlinesJson = mapper.writeValueAsString(airlines);
        airline1Pk = airlines.get(0).getAirlinePk();
    }

    @Test
    public void testLoadsAirlines() throws Exception {

        mockMvc.perform(
                post(BASE_URI).content(airlinesJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Airline> actual = airlineRepository.findAll();

        assertThat(actual, equalTo(airlines));
    }

    @Test
    public void testUpdatesExistingAirlines() throws Exception {

        createAirlines();

        Airline airline = airlines.get(0);
        airline.setGlobalName("new name");

        String agentJson = mapper.writeValueAsString(airlines);

        mockMvc.perform(post(BASE_URI).content(agentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Airline retrievedAirline = airlineRepository.findById(airline.getAirlinePk()).get();

        assertThat(retrievedAirline.getGlobalName(), equalTo("new name"));
    }

    @Test
    public void testValidatesAirlines() throws Exception {

        airlines.get(0).setGlobalName(null);

        String airlinesJson = mapper.writeValueAsString(airlines);

        mockMvc.perform(
                post(BASE_URI).content(airlinesJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testValidatesAirlinesRecursively() throws Exception {

        airlines.get(0).getAirlinePk().setIsoCountryCode("INCORRECT_ISO_COUNTRY_CODE");

        String airlinesJson = mapper.writeValueAsString(airlines);

        mockMvc.perform(
                post(BASE_URI).content(airlinesJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testGetAirlines() throws Exception {

        createAirlines();

        String responseBody = mockMvc.perform(get(BASE_URI)).andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Airline> retrievedAirlines =
                mapper.readValue(responseBody, new TypeReference<List<Airline>>() {});

        assertThat(retrievedAirlines, equalTo(airlines));
    }

    private void createAirlines() {

        airlineRepository.saveAll(airlines);
    }

    @Test
    public void testGetAirlineByCountryAndCode() throws Exception {

        createAirlines();

        String responseBody = mockMvc.perform(get(AIRLINE_1_URI)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Airline airline = airlineRepository.findById(airline1Pk).get();
        Airline retrievedAirline = mapper.readValue(responseBody, Airline.class);

        assertThat(retrievedAirline, equalTo(airline));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToGetNonExistentAirline() throws Exception {

        mockMvc.perform(get(NON_EXISTENT_AIRLINE_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeletesAirline() throws Exception {

        createAirlines();

        String responseBody = mockMvc.perform(delete(AIRLINE_1_URI)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Optional<Airline> optionalAirline = airlineRepository.findById(airline1Pk);

        assertFalse(optionalAirline.isPresent());

        Airline retrievedAirline = mapper.readValue(responseBody, Airline.class);
        assertThat(retrievedAirline, equalTo(airlines.get(0)));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToDeleteNonExistentAirline() throws Exception {

        mockMvc.perform(delete(NON_EXISTENT_AIRLINE_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testSerializesAirlineUnwrapped() throws Exception {

        createAirlines();

        mockMvc.perform(get(AIRLINE_1_URI)).andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineCode").exists())
                .andExpect(jsonPath("$.isoCountryCode").exists())
                .andExpect(jsonPath("$.address1").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.taxNumber").exists());
    }
}
