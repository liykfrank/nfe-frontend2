package org.iata.bsplink.airlinemgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AirlineTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Airline.class).verify();
    }

}
