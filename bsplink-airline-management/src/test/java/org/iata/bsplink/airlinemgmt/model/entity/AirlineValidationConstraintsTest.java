package org.iata.bsplink.airlinemgmt.model.entity;

import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getAirlinesFixture;

public class AirlineValidationConstraintsTest extends ValidationContraintTestCase<Airline> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "airlinePk", null, MUST_NOT_BE_NULL_MESSAGE },
            { "designator", null, MUST_NOT_BE_NULL_MESSAGE },
            { "designator", VERY_LONG_STRING, "size must be between 2 and 2" },
            { "globalName", null, MUST_NOT_BE_NULL_MESSAGE },
            { "globalName", VERY_LONG_STRING, "size must be between 0 and 255" },
            { "fromDate", null, MUST_NOT_BE_NULL_MESSAGE },
        };
    }

    @Override
    protected Airline getObjectToValidate() {

        return getAirlinesFixture().get(0);
    }
}
