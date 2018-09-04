package org.iata.bsplink.user.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.user.model.entity.UserTemplate;
import org.junit.Test;


public class UserTemplateTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(UserTemplate.class)
            .verify();
    }
}
