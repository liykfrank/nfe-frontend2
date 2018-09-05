package org.iata.bsplink.user.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.junit.Test;


public class BsplinkTemplateTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(BsplinkTemplate.class).verify();
    }
}
