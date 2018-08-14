package org.iata.bsplink.refund.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class RefundTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Refund.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .verify();
    }

    @Test
    public void testGetTicketDocumentNumber() {

        Refund refund = new Refund();

        assertThat(refund.getTicketDocumentNumber(), nullValue());

        refund.setId((long)1);

        assertThat(refund.getTicketDocumentNumber(), nullValue());

        refund.setAirlineCode("075");

        assertThat(refund.getTicketDocumentNumber(), equalTo("0750000000001"));
    }
}
