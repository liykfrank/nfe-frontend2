package org.iata.bsplink.agentmgmt.model.entity;

import static org.iata.bsplink.agentmgmt.test.fixtures.AgentFixtures.getAgentsFixture;

public class AgentValidationConstraintsTest extends ValidationContraintTestCase<Agent> {

    private static final String VALID_CONTROL_DIGIT_MESSAGE =
            "must be a numeric value ending with a control digit";

    private static final String WRONG_LENGTH_IATA_CODE = "123456782";
    private static final String WRONG_CONTROL_DIGIT_IATA_CODE = "12345678";

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "name", null, MUST_NOT_BE_NULL_MESSAGE },
            { "name", VERY_LONG_STRING, "size must be between 3 and 255" },
            { "name", "ab", getSizeValidationErrorMessage(3, 255) },
            { "iataCode", null, MUST_NOT_BE_NULL_MESSAGE},
            { "iataCode", WRONG_LENGTH_IATA_CODE, getSizeValidationErrorMessage(8, 8) },
            { "iataCode", WRONG_CONTROL_DIGIT_IATA_CODE, VALID_CONTROL_DIGIT_MESSAGE },
            { "accreditationDate", null, MUST_NOT_BE_NULL_MESSAGE},
            { "taxId", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 20) },
            { "tradeName", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 100) },
            { "billingCity", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 40) },
            { "billingCountry", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 80) },
            { "billingPostalCode", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 20) },
            { "billingState", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 80) },
            { "phone", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 40) },
            { "email", "foo", "must be a well-formed email address" },
            { "fax", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 40) },
            { "isoCountryCode", null, MUST_NOT_BE_NULL_MESSAGE},
            { "isoCountryCode", VERY_LONG_STRING, getSizeValidationErrorMessage(2, 2) },
            { "parentIataCode", WRONG_LENGTH_IATA_CODE, getSizeValidationErrorMessage(8, 8) },
            { "parentIataCode", WRONG_CONTROL_DIGIT_IATA_CODE, VALID_CONTROL_DIGIT_MESSAGE },
            { "topParentIataCode", WRONG_LENGTH_IATA_CODE, getSizeValidationErrorMessage(8, 8) },
            { "topParentIataCode", WRONG_CONTROL_DIGIT_IATA_CODE, VALID_CONTROL_DIGIT_MESSAGE },
            { "vatNumber", VERY_LONG_STRING, getSizeValidationErrorMessage(0, 30) },
        };
    }

    @Override
    protected Agent getObjectToValidate() {

        return getAgentsFixture().get(0);
    }
}
