package org.iata.bsplink.refund.fake;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class CountryTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Country.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
