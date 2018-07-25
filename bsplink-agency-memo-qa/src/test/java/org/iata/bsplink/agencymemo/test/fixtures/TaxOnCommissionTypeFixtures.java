package org.iata.bsplink.agencymemo.test.fixtures;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionTypePk;

public class TaxOnCommissionTypeFixtures {

    /**
     * Returns a list of Tax on Commission Types.
     */
    public static List<TaxOnCommissionType> getTaxOnCommissionTypes() {

        TaxOnCommissionTypePk tctpPk1 = new TaxOnCommissionTypePk();
        tctpPk1.setIsoCountryCode("AA");
        tctpPk1.setCode("CODE1");
        TaxOnCommissionType tctp1 = new TaxOnCommissionType();
        tctp1.setPk(tctpPk1);
        tctp1.setDescription("Description 1");

        TaxOnCommissionTypePk tctpPk2 = new TaxOnCommissionTypePk();
        tctpPk2.setIsoCountryCode("AA");
        tctpPk2.setCode("CODE2");
        TaxOnCommissionType tctp2 = new TaxOnCommissionType();
        tctp2.setPk(tctpPk2);
        tctp2.setDescription("Description 2");

        TaxOnCommissionTypePk tctpPk3 = new TaxOnCommissionTypePk();
        tctpPk3.setIsoCountryCode("BB");
        tctpPk3.setCode("CODE1");
        TaxOnCommissionType tctp3 = new TaxOnCommissionType();
        tctp3.setPk(tctpPk3);
        tctp3.setDescription("Description 1");

        return Arrays.asList(tctp1, tctp2, tctp3);
    }

}
