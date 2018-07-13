package org.iata.bsplink.refund.loader.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class RefundDocumentTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(RefundDocument.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
