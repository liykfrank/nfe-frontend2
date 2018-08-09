package org.iata.bsplink.refund.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class RefundIssuePermissionTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RefundIssuePermission.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}
