package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class TaxMiscellaneousFeeTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TaxMiscellaneousFee.class).verify();
    }
}
