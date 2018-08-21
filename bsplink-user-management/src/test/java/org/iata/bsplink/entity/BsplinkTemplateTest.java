package org.iata.bsplink.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.junit.Test;


public class BsplinkTemplateTest {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(BsplinkOption.class).verify();
    }
}
