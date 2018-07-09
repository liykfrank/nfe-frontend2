package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class ConfigTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(Config.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
