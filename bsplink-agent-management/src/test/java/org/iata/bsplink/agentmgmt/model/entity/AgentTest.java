package org.iata.bsplink.agentmgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AgentTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Agent.class).verify();
    }

}
