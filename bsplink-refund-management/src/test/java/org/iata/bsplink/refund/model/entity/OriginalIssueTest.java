package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class OriginalIssueTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(OriginalIssue.class).verify();
    }
}
