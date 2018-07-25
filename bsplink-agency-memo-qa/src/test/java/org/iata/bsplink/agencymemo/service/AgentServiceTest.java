package org.iata.bsplink.agencymemo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.iata.bsplink.agencymemo.test.fixtures.AgentFixtures.getAgents;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.exception.FeignClientException;
import org.iata.bsplink.agencymemo.restclient.AgentClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AgentServiceTest {

    private AgentService agentService;
    private Agent agent;
    private ResponseEntity<Agent> respAgent;
    private ResponseEntity<List<Agent>> respAgents;
    private String notFoundAgentCode;
    private String errorAgentCode;

    @Before
    public void setUp() {
        agent = getAgents().get(0);

        notFoundAgentCode = "00000070";
        errorAgentCode = "ERROR";

        AgentClient agentClient = mock(AgentClient.class);

        respAgent = ResponseEntity.status(HttpStatus.OK).body(agent);
        when(agentClient.findAgent(agent.getIataCode())).thenReturn(respAgent);

        ResponseEntity<Agent> notFound = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(agentClient.findAgent(notFoundAgentCode)).thenReturn(notFound);

        ResponseEntity<Agent> badRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        when(agentClient.findAgent(errorAgentCode)).thenReturn(badRequest);

        respAgents = ResponseEntity.status(HttpStatus.OK).body(getAgents());
        when(agentClient.findAgents()).thenReturn(respAgents);
        agentService = new AgentService(agentClient);
    }


    @Test
    public void testFindAgentResponse() throws Exception {
        ResponseEntity<Agent> respAgentFound = agentService.findAgentResponse(agent.getIataCode());
        assertThat(respAgentFound, is(notNullValue()));
        assertThat(respAgentFound.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(respAgentFound.getBody(), is(notNullValue()));
        assertThat(respAgentFound.getBody(), equalTo(agent));
    }

    @Test
    public void testFindAgentResponseNotFound() throws Exception {
        ResponseEntity<Agent> respAgentFound = agentService.findAgentResponse(notFoundAgentCode);
        assertThat(respAgentFound, is(notNullValue()));
        assertThat(respAgentFound.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testFindAgent() throws Exception {
        Agent agentFound = agentService.findAgent(agent.getIataCode());
        assertThat(agentFound, is(notNullValue()));
        assertThat(agentFound, equalTo(agent));
    }

    @Test
    public void findAllAgentResponse() throws Exception {
        ResponseEntity<List<Agent>> agentsFound = agentService.findAllAgentResponse();
        assertThat(agentsFound, is(notNullValue()));
        assertThat(agentsFound.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(agentsFound, equalTo(respAgents));
    }

    @Test
    public void testFindAgentNotFound() throws Exception {
        Agent agentFound = agentService.findAgent(notFoundAgentCode);
        assertThat(agentFound, is(nullValue()));
    }

    @Test (expected = FeignClientException.class)
    public void testFindAgentBadRequest() throws Exception {
        agentService.findAgent(errorAgentCode);
    }
}
