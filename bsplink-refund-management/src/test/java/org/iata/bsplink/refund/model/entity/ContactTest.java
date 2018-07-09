package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class ContactTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Contact.class).verify();
    }
}
