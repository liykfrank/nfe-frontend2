package org.iata.bsplink.agencymemo.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.agencymemo.test.fixtures.TaxOnCommissionTypeFixtures.getTaxOnCommissionTypes;
import static org.junit.Assert.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;


public class TaxOnCommissionTypeTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TaxOnCommissionType.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testGetCode() {
        TaxOnCommissionType tctp = getTaxOnCommissionTypes().get(0);
        assertThat(tctp.getCode(), equalTo(tctp.getPk().getCode()));
    }

    @Test
    public void testGetIsoCountryCode() {
        TaxOnCommissionType tctp = getTaxOnCommissionTypes().get(0);
        assertThat(tctp.getIsoCountryCode(), equalTo(tctp.getPk().getIsoCountryCode()));
    }
}
