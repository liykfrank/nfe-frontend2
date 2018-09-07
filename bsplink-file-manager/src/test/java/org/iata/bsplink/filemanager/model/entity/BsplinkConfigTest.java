package org.iata.bsplink.filemanager.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class BsplinkConfigTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BsplinkConfig.class).verify();
    }
}
