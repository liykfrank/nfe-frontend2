package org.iata.bsplink.airlinemgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AirlinePkTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AirlinePk.class).verify();
    }

}
