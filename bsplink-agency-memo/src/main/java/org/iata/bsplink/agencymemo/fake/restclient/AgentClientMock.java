package org.iata.bsplink.agencymemo.fake.restclient;

import static org.iata.bsplink.agencymemo.fake.restclient.ClientMockFixtures.getAgents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.restclient.AgentClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Mock client with develop purposes.
 */
@Component
@Profile("mock")
public class AgentClientMock implements AgentClient {

    Map<String, Agent> agents = getAgents();

    @Override
    public ResponseEntity<Agent> findAgent(String code) {

        if (!agents.containsKey(code)) {

            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(agents.get(code), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Agent>> findAgents() {

        return new ResponseEntity<>(new ArrayList<>(agents.values()), HttpStatus.OK);
    }

}
