package org.iata.bsplink.agencymemo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.agencymemo.fake.Period;
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
public class GeneralInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webAppContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
    }

    @Test
    public void testGetAllPeriods() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(getPeriodsList());

        mockMvc.perform(get("/v1/general-info/periods")).andExpect(content().json(jsonInString))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPeriodsByIsoc() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(getPeriodsList().get(0));

        mockMvc.perform(get("/v1/general-info/periods/AA")).andExpect(content().json(jsonInString))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetPeriodsByIsocNotFound() throws Exception {
        mockMvc.perform(get("/v1/general-info/periods/AZ")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllCurrencies() throws Exception {

        String jsonInString = "[{\"isoc\":\"AA\",\"currencies\":[{\"name\":\"ABC\","
                + "\"numDecimals\":2,\"expirationDate\":\"2058-12-24\"},"
                + "{\"name\":\"DEF\",\"numDecimals\":3,\"expirationDate\":"
                + "\"2058-12-25\"},{\"name\":\"GHI\",\"numDecimals\":0"
                + ",\"expirationDate\":\"2058-12-25\"},{\"name\":\"JKL\",\"numDecimals\":1"
                + ",\"expirationDate\":\"2058-12-25\"},{\"name\":\"ZZZ\",\"numDecimals\":0"
                + ",\"expirationDate\":\"2017-01-01\"}]},{\"isoc\"" + ":\"BB\""
                + ",\"currencies\":[{\"name\":\"ZZZ\","
                + "\"numDecimals\":0,\"expirationDate\":\"2017-01-01\"}]}"
                + ",{\"isoc\":\"CC\",\"currencies\":[{\"name\":\"ERR\","
                + "\"numDecimals\":2,\"expirationDate\":\"2031-01-01\"}]}]\r\n";


        mockMvc.perform(get("/v1/general-info/currencies")).andExpect(content().json(jsonInString))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCurrencies() throws Exception {

        String jsonInString = "[{\"isoc\":\"AA\",\"currencies\":[{\"name\":\"ABC\","
                + "\"numDecimals\":2,\"expirationDate\":" + "\"2058-12-24\"},{\"name\":\"DEF\","
                + "\"numDecimals\":3,\"expirationDate\":" + "\"2058-12-25\"},{\"name\":\"GHI\""
                + ",\"numDecimals\":0,\"expirationDate\"" + ":\"2058-12-25\"},{\"name\":\"JKL\""
                + ",\"numDecimals\":1,\"expirationDate\"" + ":\"2058-12-25\"},{\"name\":\"ZZZ\""
                + ",\"numDecimals\":0,\"expirationDate\"" + ":\"2017-01-01\"}]}]";

        mockMvc.perform(get("/v1/general-info/currencies/AA"))
                .andExpect(content().json(jsonInString)).andExpect(status().isOk());

    }


    @Test
    public void testGetCurrenciesNotFound() throws Exception {
        mockMvc.perform(get("/v1/general-info/currencies/ZT")).andExpect(status().isNotFound());

    }

    /**
     * Get a list of all periods and ISOCS.
     *
     * @return listPeriods List.
     */
    public static List<Period> getPeriodsList() {

        List<Period> listPeriods = new ArrayList<>();

        Period aaPeriod = new Period();
        aaPeriod.setPeriod("AA");
        listPeriods.add(aaPeriod);

        Period bbPeriod = new Period();
        bbPeriod.setPeriod("BB");
        listPeriods.add(bbPeriod);

        Period ccPeriod = new Period();
        ccPeriod.setPeriod("CC");
        listPeriods.add(ccPeriod);

        return listPeriods;
    }
}
