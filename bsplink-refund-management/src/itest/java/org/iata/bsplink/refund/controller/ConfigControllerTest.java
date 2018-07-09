package org.iata.bsplink.refund.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.refund.test.fixtures.ConfigFixtures.getConfigs;
import static org.iata.bsplink.refund.test.fixtures.ConfigFixtures.getCountries;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import javax.transaction.Transactional;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.repository.ConfigRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConfigControllerTest {

    private static final String BASE_URI = "/v1/configurations";

    private List<Config> configs;
    private Config anyConfig;
    private String configJson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConfigRepository configRepository;

    @Before
    public void setUp() throws Exception {

        configs = getConfigs();
        anyConfig = configs.get(0);
        configJson = mapper.writeValueAsString(anyConfig);
    }

    @Test
    public void testSave() throws Exception {

        mockMvc.perform(
                post(BASE_URI).content(configJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Config> configs = configRepository.findAll();

        assertThat(configs, hasSize(1));
        assertThat(configs.get(0), equalTo(anyConfig));
    }

    @Test
    public void testReturnsSavedConfig() throws Exception {

        String responseBody = mockMvc.perform(
                post(BASE_URI).content(configJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getRequest().getContentAsString();

        assertThat(readConfigFromJson(responseBody), equalTo(anyConfig));
    }

    private Config readConfigFromJson(String json) throws Exception {

        return mapper.readValue(json, Config.class);
    }

    @Test
    public void testGetConfigs() throws Exception {

        createConfigs();

        String responseBody = mockMvc.perform(
                get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Config> actual = mapper.readValue(responseBody, new TypeReference<List<Config>>() {});

        assertThat(actual, equalTo(configs));
    }

    private List<Config> createConfigs() {

        List<Config> savedConfigs = configRepository.saveAll(configs);
        configRepository.flush();

        return savedConfigs;
    }


    @Test
    public void testGetConfig() throws Exception {

        Config config = createConfigs().get(0);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + config.getIsoCountryCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Config actual = mapper.readValue(responseBody, Config.class);

        assertThat(actual, equalTo(config));
    }

    @Test
    public void testReturnsDefaultWhenTryingToRetrieveNonExistentConfig() throws Exception {

        Config config = new Config("XX");

        assertFalse(configRepository.findById(config.getIsoCountryCode()).isPresent());

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + config.getIsoCountryCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Config actual = mapper.readValue(responseBody, Config.class);

        assertThat(actual, equalTo(config));
    }

    @Test
    public void testGetIsocs() throws Exception {

        createConfigs();

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/isocs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<String> actual = mapper.readValue(responseBody, new TypeReference<List<String>>() {});

        assertThat(actual, equalTo(getCountries()));
    }

    @Test
    public void testValidatesConfig() throws Exception {

        anyConfig.setAirlineVatNumberEnabled(null);

        String configJson = mapper.writeValueAsString(anyConfig);

        mockMvc.perform(
                post(BASE_URI).content(configJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }
}
