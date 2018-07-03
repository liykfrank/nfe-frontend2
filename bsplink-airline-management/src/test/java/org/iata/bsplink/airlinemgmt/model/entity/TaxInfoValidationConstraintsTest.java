package org.iata.bsplink.airlinemgmt.model.entity;

import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.AIRLINE_1_CODE;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getTaxInfoFixture;

public class TaxInfoValidationConstraintsTest
        extends ValidationContraintTestCase<TaxInfo> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "taxNumber", VERY_LONG_STRING, "size must be between 0 and 60" },
        };
    }

    @Override
    protected TaxInfo getObjectToValidate() {

        return getTaxInfoFixture(AIRLINE_1_CODE);
    }
}
