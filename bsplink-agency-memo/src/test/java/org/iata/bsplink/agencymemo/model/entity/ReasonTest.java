package org.iata.bsplink.agencymemo.model.entity;

import static org.iata.bsplink.agencymemo.test.fixtures.ReasonFixtures.getReasons;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.iata.bsplink.agencymemo.test.validation.ValidationContraintTestCase;
import org.junit.Test;

public class ReasonTest extends ValidationContraintTestCase<Reason> {

    @Test
    public void equalsContract() {

        EqualsVerifier.forClass(Reason.class).withIgnoredFields("id").verify();
    }

    @Override
    protected Reason getObjectToValidate() {

        return getReasons().get(0);
    }

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {
            { "title", null, MUST_NOT_BE_NULL_MESSAGE },
            { "title", VERY_LONG_STRING, "size must be between 3 and 255" },
            { "detail", null, MUST_NOT_BE_NULL_MESSAGE },
            { "detail", String.format("very long %8000s ", "string"),
                "size must be between 3 and 4500" },
            { "isoCountryCode", null, MUST_NOT_BE_NULL_MESSAGE },
            { "isoCountryCode", VERY_LONG_STRING, "size must be between 2 and 2" }
        };
    }

}
