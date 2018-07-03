package org.iata.bsplink.agencymemo.model.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.iata.bsplink.agencymemo.test.fixtures.AcdmFixtures.getAcdms;
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
    public void postLoad() {
        Acdm acdm = getAcdms().get(0);
        assertThat(acdm.getTicketDocumentNumber(), is(nullValue()));
        acdm.postLoad();
        assertThat(acdm.getTicketDocumentNumber(), is(notNullValue()));
    }
}
