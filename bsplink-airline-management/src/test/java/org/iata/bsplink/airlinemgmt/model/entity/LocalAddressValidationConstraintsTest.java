package org.iata.bsplink.airlinemgmt.model.entity;

import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.AIRLINE_1_CODE;
import static org.iata.bsplink.airlinemgmt.test.fixtures.AirlineFixtures.getLocalAddressFixture;

public class LocalAddressValidationConstraintsTest
        extends ValidationContraintTestCase<LocalAddress> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "address1", VERY_LONG_STRING, "size must be between 0 and 50" },
            { "city", VERY_LONG_STRING, "size must be between 0 and 50" },
            { "state", VERY_LONG_STRING, "size must be between 0 and 50" },
            { "country", VERY_LONG_STRING, "size must be between 2 and 2" },
            { "postalCode", VERY_LONG_STRING, "size must be between 0 and 10" }
        };
    }

    @Override
    protected LocalAddress getObjectToValidate() {

        return getLocalAddressFixture(AIRLINE_1_CODE);
    }
}
