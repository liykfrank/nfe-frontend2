package org.iata.bsplink.filemanager.pojo;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class UserTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(User.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }
}
