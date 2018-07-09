package org.iata.bsplink.refund.model.entity;

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
}
