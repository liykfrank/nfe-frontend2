package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class RelatedDocumentTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RelatedDocument.class).verify();
    }
}
