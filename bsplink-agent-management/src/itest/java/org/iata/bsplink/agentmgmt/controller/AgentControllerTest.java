package org.iata.bsplink.agentmgmt.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.agentmgmt.test.fixtures.AgentFixtures.IATA_CODE_1;
import static org.iata.bsplink.agentmgmt.test.fixtures.AgentFixtures.IATA_CODE_2;
import static org.iata.bsplink.agentmgmt.test.fixtures.AgentFixtures.getAgentsFixture;
import static org.junit.Assert.assertEquals;
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

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.iata.bsplink.agentmgmt.model.repository.AgentRepository;
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
public class AgentControllerTest {

    private static final String BASE_URI = "/v1/agents";
    private static final String NON_EXISTENT_AGENT_URI = BASE_URI + "/78787878";
    private static final String IATA_CODE_1_URI = "/v1/agents/" + IATA_CODE_1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected WebApplicationContext webAppContext;

    private List<Agent> agents;
    private String agentsJson;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
        agentRepository.deleteAll();
        agents = getAgentsFixture();
        agentsJson = mapper.writeValueAsString(agents);
    }

    @Test
    public void testLoadAgent() throws Exception {
        mockMvc.perform(post(BASE_URI).content(agentsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<Agent> actual = agentRepository.findAll();

        // There is a problem with the comparison for equality because of the added entity ids,
        // that is the reason why it's done this way
        assertThat(actual, hasSize(agents.size()));
        assertEquals(agents.get(0), findAgent(IATA_CODE_1, actual));
        assertEquals(agents.get(1), findAgent(IATA_CODE_2, actual));
    }

    private Agent findAgent(String iataCode, List<Agent> list) {

        return list.stream().filter(value -> value != null)
                .filter(value -> iataCode.equals(value.getIataCode())).findFirst().get();
    }

    @Test
    public void testUpdatesExistingAgents() throws Exception {

        createAgents();

        Agent agent = agents.get(0);
        agent.setEmail("new-email@example.com");

        String agentJson = mapper.writeValueAsString(agents);

        mockMvc.perform(post(BASE_URI).content(agentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Agent retrievedAgent = agentRepository.findById(IATA_CODE_1).get();

        assertThat(retrievedAgent.getEmail(), equalTo("new-email@example.com"));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToGetNonExistentAgent() throws Exception {
        mockMvc.perform(get(NON_EXISTENT_AGENT_URI)).andExpect(status().isNotFound());
    }

    private void createAgents() {
        agentRepository.saveAll(agents);
    }

    @Test
    public void testGetAgent() throws Exception {
        createAgents();
        String responseBody = mockMvc.perform(get(IATA_CODE_1_URI)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Agent agent = agentRepository.findById(IATA_CODE_1).get();
        Agent retrievedAgent = mapper.readValue(responseBody, Agent.class);
        assertThat(retrievedAgent, equalTo(agent));
    }

    @Test
    public void testGetAgents() throws Exception {
        createAgents();
        String responseBody = mockMvc.perform(get(BASE_URI)).andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Agent> createdAccount =
                mapper.readValue(responseBody, new TypeReference<List<Agent>>() {});
        assertThat(createdAccount, equalTo(agents));
    }

    @Test
    public void testValidatesAgents() throws Exception {

        Agent agent = agents.get(0);
        agent.setIataCode(null);

        String agentJson = mapper.writeValueAsString(agents);

        mockMvc.perform(post(BASE_URI).content(agentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testValidatesAgentsRecursively() throws Exception {

        Agent agent = agents.get(0);
        agent.getFormOfPayment().get(0).setStatus(null);;

        String agentJson = mapper.writeValueAsString(agents);

        mockMvc.perform(post(BASE_URI).content(agentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testDeletesAgent() throws Exception {

        createAgents();

        String responseBody = mockMvc.perform(delete(IATA_CODE_1_URI)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Optional<Agent> optionalAgent = agentRepository.findById(IATA_CODE_1);

        assertFalse(optionalAgent.isPresent());

        Agent retrievedAgent = mapper.readValue(responseBody, Agent.class);
        assertThat(retrievedAgent, equalTo(agents.get(0)));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToDeleteNonExistentAgent() throws Exception {

        mockMvc.perform(delete(NON_EXISTENT_AGENT_URI)).andExpect(status().isNotFound());
    }
}
