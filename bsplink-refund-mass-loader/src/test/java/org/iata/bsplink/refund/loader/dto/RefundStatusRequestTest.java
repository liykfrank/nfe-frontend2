package org.iata.bsplink.refund.loader.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class RefundStatusRequestTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(RefundStatusRequest.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
