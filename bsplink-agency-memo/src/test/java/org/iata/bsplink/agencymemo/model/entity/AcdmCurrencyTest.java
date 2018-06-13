package org.iata.bsplink.agencymemo.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;


public class AcdmCurrencyTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AcdmCurrency.class).verify();
    }
}
