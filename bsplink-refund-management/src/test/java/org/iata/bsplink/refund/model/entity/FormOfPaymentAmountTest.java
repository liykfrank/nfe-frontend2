package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class FormOfPaymentAmountTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(FormOfPaymentAmount.class).verify();
    }
}
