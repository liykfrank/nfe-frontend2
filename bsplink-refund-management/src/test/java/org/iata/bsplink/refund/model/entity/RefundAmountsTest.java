package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class RefundAmountsTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RefundAmounts.class).verify();
    }
}
