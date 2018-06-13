package org.iata.bsplink.agencymemo.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class ContactRequestTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(ContactRequest.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
