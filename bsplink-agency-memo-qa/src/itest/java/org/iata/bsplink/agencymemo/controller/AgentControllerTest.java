package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.agencymemo.test.fixtures.AgentFixtures.getAgents;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.service.AgentService;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTest {

    private static final String BASE_URI = "/v1/agents";
    private static final String NON_EXISTENT_AGENT_URI = "/v1/agents/00000011";

    private Agent agent;
    private List<Agent> agents;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AgentService agentService;

    @Before
    public void setUp() throws Exception {
        agent = getAgents().get(0);
        agents = getAgents();
        ResponseEntity<Agent> respAgent = ResponseEntity.status(HttpStatus.OK).body(agent);
        when(agentService.findAgentResponse(agent.getIataCode())).thenReturn(respAgent);

        ResponseEntity<Agent> notFound = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(agentService.findAgentResponse("00000011")).thenReturn(notFound);

        ResponseEntity<List<Agent>>  respAgents = ResponseEntity.status(HttpStatus.OK).body(agents);
        when(agentService.findAllAgentResponse()).thenReturn(respAgents);
    }

    @Test
    public void testGetAgent() throws Exception {
        String responseBody = mockMvc.perform(
            get(BASE_URI + "/" + agent.getIataCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Agent actual = mapper.readValue(responseBody, Agent.class);

        assertThat(actual, equalTo(agent));
    }

    @Test
    public void testGetAgentNotFound() throws Exception {
        mockMvc.perform(get(NON_EXISTENT_AGENT_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAgents() throws Exception {
        String responseBody = mockMvc.perform(
            get(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Agent> actual = mapper.readValue(responseBody, new TypeReference<List<Agent>>() {});

        assertThat(actual, equalTo(agents));
    }


}
