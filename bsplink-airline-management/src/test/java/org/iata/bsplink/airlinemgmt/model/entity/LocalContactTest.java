package org.iata.bsplink.airlinemgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class LocalContactTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(LocalContact.class).verify();
    }

}
