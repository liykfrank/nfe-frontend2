package org.iata.bsplink.refund.test.fixtures;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.refund.dto.Agent;

public class AgentFixtures {

    /**
     * Returns a list of Agents.
     */
    public static List<Agent> getAgents() {
        Agent a1 = new Agent();
        a1.setIataCode("78200102");
        a1.setIsoCountryCode("AA");
        a1.setName("AGENCIA");
        Agent a2 = new Agent();
        a2.setIataCode("00000700");
        a2.setIsoCountryCode("JB");
        a2.setName("SECRET_AGENT");
        return Arrays.asList(a1, a2);
    }

}
