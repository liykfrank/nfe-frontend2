package org.iata.bsplink.agencymemo.fake;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class CurrencyTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Currency.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
