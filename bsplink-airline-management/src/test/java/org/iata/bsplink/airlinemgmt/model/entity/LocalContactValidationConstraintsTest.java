package org.iata.bsplink.airlinemgmt.model.entity;

import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.AIRLINE_1_CODE;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getLocalContactFixture;

public class LocalContactValidationConstraintsTest
        extends ValidationContraintTestCase<LocalContact> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "firstName", VERY_LONG_STRING, "size must be between 0 and 255" },
            { "lastName", VERY_LONG_STRING, "size must be between 0 and 255" },
            { "email", "foo", MUST_BE_VALID_EMAIL_MESSAGE },
            { "telephone", VERY_LONG_STRING, "size must be between 0 and 15" },
        };
    }

    @Override
    protected LocalContact getObjectToValidate() {

        return getLocalContactFixture(AIRLINE_1_CODE);
    }
}
