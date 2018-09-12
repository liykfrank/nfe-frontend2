package org.iata.bsplink.user.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import org.iata.bsplink.user.preferences.TimeZones;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeZonesControllerTest {

    private static final String TIME_ZONES_URL = "/v1/users/timezones";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    protected WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
    }

    @Test
    public void testListsTimeZones() throws Exception {

        String timeZonesJson = mockMvc.perform(get(TIME_ZONES_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<String> timeZones =
                MAPPER.readValue(timeZonesJson, new TypeReference<Set<String>>() {});

        assertThat(timeZones, equalTo(TimeZones.timeZones()));
    }

}
