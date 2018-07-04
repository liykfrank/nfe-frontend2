package org.iata.bsplink.refund.loader.dto;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class RefundTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Refund.class);
    }

}
