package org.iata.bsplink.refund.service;

import java.util.List;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.exception.FeignClientException;
import org.iata.bsplink.refund.restclient.AgentClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private AgentClient agentClient;

    public AgentService(AgentClient agentClient) {
        super();
        this.agentClient = agentClient;
    }

    public ResponseEntity<Agent> findAgentResponse(String code) {
        return agentClient.findAgent(code);
    }

    public ResponseEntity<List<Agent>> findAllAgentResponse() {
        return agentClient.findAgents();
    }

    /**
     * Returns an Agent.
     */
    public Agent findAgent(String code) {
        ResponseEntity<Agent> respAgent = findAgentResponse(code);

        if (!respAgent.getStatusCode().isError()) {
            return respAgent.getBody();
        }

        if (respAgent.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return null;
        }

        throw new FeignClientException(respAgent.getStatusCode());
    }

}
