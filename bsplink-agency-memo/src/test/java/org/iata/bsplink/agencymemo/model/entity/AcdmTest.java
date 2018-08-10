package org.iata.bsplink.agencymemo.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class AcdmTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Acdm.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .suppress(Warning.TRANSIENT_FIELDS)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testGetTicketDocumentNumber() {

        Acdm acdm = new Acdm();

        assertThat(acdm.getTicketDocumentNumber(), nullValue());

        acdm.setId((long)1);

        assertThat(acdm.getTicketDocumentNumber(), nullValue());

        acdm.setAirlineCode("075");

        assertThat(acdm.getTicketDocumentNumber(), equalTo("0750000000001"));
    }
}
