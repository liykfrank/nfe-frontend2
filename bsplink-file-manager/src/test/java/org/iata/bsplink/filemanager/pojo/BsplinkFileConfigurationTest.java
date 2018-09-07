package org.iata.bsplink.filemanager.pojo;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class BsplinkFileConfigurationTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BsplinkFileConfiguration.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }
}
