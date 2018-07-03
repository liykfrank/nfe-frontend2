package org.iata.bsplink.agentmgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class FormOfPaymentTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(FormOfPayment.class)
            .withIgnoredFields("id")
            .verify();
    }

}
