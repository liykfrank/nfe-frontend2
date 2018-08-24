package org.iata.bsplink.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.junit.Test;


public class UserTemplateTest {

    @Test
    public void equalsContract() {

        User user1 = new User();
        user1.setId("U1");
        User user2 = new User();
        user2.setId("U2");

        BsplinkTemplate bt1 = new BsplinkTemplate();
        bt1.setId("1");
        BsplinkTemplate bt2 = new BsplinkTemplate();
        bt2.setId("2");

        //TODO remove userId
        EqualsVerifier.forClass(UserTemplate.class)
            .withPrefabValues(BsplinkTemplate.class, bt1, bt2)
            .verify();
    }
}
