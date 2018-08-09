package org.iata.bsplink.refund.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.iata.bsplink.refund.dto.CommentRequest;
import org.junit.Test;

public class CommentRequestTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CommentRequest.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }

}
