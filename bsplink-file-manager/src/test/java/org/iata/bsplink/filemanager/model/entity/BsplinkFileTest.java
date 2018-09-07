package org.iata.bsplink.filemanager.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class BsplinkFileTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BsplinkFile.class).verify();
    }
}
