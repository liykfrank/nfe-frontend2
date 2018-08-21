package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import javax.transaction.Transactional;

import org.iata.bsplink.agencymemo.fake.Country;
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
public class CountryControllerTest {

    private static final String BASE_URI = "/v1/countries";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    protected WebApplicationContext webAppContext;
    
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
    }

    @Test
    public void testGetCountries() throws Exception {

        List<Country> countries = Country.findAllCountries();

        String body = mockMvc.perform(get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Country> actual = mapper.readValue(body, new TypeReference<List<Country>>() {});

        assertThat(actual, equalTo(countries));
    }


}
