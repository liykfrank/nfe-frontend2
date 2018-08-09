package org.iata.bsplink.agencymemo.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.iata.bsplink.agencymemo.test.validation.ValidationContraintTestCase;
import org.junit.Test;


public class TaxOnCommissionTypeTest extends ValidationContraintTestCase<TaxOnCommissionType> {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TaxOnCommissionType.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS).verify();
    }


    @Override
    protected TaxOnCommissionType getObjectToValidate() {

        TaxOnCommissionTypePk pk = new TaxOnCommissionTypePk();
        pk.setIsoCountryCode("AA");
        pk.setCode("CODE");
        TaxOnCommissionType tctp = new TaxOnCommissionType();
        tctp.setPk(pk);
        tctp.setDescription("description");
        return tctp;
    }


    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "description", null, MUST_NOT_BE_NULL_MESSAGE },
            { "description", "ABCD !", TaxOnCommissionType.DESCRIPTION_FORMAT },
            { "pk.code", null, MUST_NOT_BE_NULL_MESSAGE },
            { "pk.code", "1234567890", "size must be between 1 and 6" },
            { "pk.code", "abcd$#", TaxOnCommissionTypePk.CODE_FORMAT },
            { "pk.isoCountryCode", null, MUST_NOT_BE_NULL_MESSAGE },
        };
    }
}
