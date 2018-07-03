package org.iata.bsplink.airlinemgmt.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class TaxInfoTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TaxInfo.class).verify();
    }

}
