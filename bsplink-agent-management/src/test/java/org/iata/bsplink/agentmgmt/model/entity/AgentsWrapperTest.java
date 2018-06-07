package org.iata.bsplink.agentmgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AgentsWrapperTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AgentsWrapper.class).verify();
    }

}
