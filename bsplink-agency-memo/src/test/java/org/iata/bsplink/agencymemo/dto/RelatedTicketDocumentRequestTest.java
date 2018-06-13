package org.iata.bsplink.agencymemo.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class RelatedTicketDocumentRequestTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RelatedTicketDocumentRequest.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
