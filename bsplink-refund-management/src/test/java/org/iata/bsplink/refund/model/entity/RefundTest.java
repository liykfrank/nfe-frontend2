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

        Refund acdm = new Refund();

        assertThat(acdm.getTicketDocumentNumber(), nullValue());

        acdm.setId((long)1);

        assertThat(acdm.getTicketDocumentNumber(), equalTo("0000000001"));
    }
}
