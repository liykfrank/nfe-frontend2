package org.iata.bsplink.refund.test.validation;

public abstract class RefundStringValueIsEnabledValidatorTestCase
        extends RefundValueIsEnabledValidatorTestCase<String> {

    @Override
    protected Object[][] validationCases() {

        return new Object[][] {
            { getDefinedValue(), true, false },
            { null, true, false },
            { "", true, false },
            { getDefinedValue(), false, true }
        };
    }

    @Override
    protected String getDefinedValue() {

        return "1";
    }

}
