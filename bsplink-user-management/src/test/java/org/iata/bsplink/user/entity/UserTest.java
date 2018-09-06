package org.iata.bsplink.user.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.user.model.entity.User;
import org.junit.Test;


public class UserTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(User.class).verify();
    }
}
