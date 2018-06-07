package org.iata.bsplink.airlinemgmt.model.entity;

import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getAirlinesFixture;

public class AirlinePkValidationConstraintsTest extends ValidationContraintTestCase<AirlinePk> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "airlineCode", null, MUST_NOT_BE_NULL_MESSAGE },
            { "isoCountryCode", VERY_LONG_STRING, "size must be between 2 and 2" },
        };
    }

    @Override
    protected AirlinePk getObjectToValidate() {

        return getAirlinesFixture().get(0).getAirlinePk();
    }
}
