package org.iata.bsplink.agencymemo.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class ContactTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Contact.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
