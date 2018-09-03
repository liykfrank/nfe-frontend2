package org.iata.bsplink.refund.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CurrenciesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
    }

    @Test
    public void testGetCurrencies() throws Exception {
        String json = "[{\"isoc\":\"AA\",\"currencies\":[{\"name\":\"ABC\",\"numDecimals\":2,"
                + "\"expirationDate\":\"2058-12-24\"},{\"name\":\"DEF\",\"numDecimals\":3,"
                + "\"expirationDate\":\"2058-12-25\"},{\"name\":\"GHI\",\"numDecimals\":0,"
                + "\"expirationDate\":\"2058-12-25\"},{\"name\":\"JKL\",\"numDecimals\":1,"
                + "\"expirationDate\":\"2058-12-25\"},{\"name\":\"ZZZ\",\"numDecimals\":0,"
                + "\"expirationDate\":\"2017-01-01\"}]}]";
        mockMvc.perform(get("/v1/currencies/AA")).andExpect(status().isOk())
                .andExpect(content().json(json));
    }


    @Test
    public void testGetCurrenciesNotFound() throws Exception {
        mockMvc.perform(get("/v1/currencies/ZT")).andExpect(status().isNotFound());
    }
}
