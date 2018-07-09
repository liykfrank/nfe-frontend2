package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class RefundCurrencyTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RefundCurrency.class).verify();
    }
}
