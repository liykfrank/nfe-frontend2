package org.iata.bsplink.airlinemgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class LocalAddressTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(LocalAddress.class).verify();
    }

}
