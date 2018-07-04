package org.iata.bsplink.refund.loader.model;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class RefundDocumentTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RefundDocument.class);
    }

}
