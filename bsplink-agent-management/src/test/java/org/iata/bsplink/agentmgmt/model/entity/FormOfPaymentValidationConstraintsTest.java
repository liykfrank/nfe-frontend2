package org.iata.bsplink.agentmgmt.model.entity;

public class FormOfPaymentValidationConstraintsTest
        extends ValidationContraintTestCase<FormOfPayment> {

    @Override
    protected Object[][] parametersForTestValidationConstraints() {

        return new Object[][] {

            { "type", null, MUST_NOT_BE_NULL_MESSAGE },
            { "status", null, MUST_NOT_BE_NULL_MESSAGE },
        };
    }

    @Override
    protected FormOfPayment getObjectToValidate() {

        return new FormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.ACTIVE);
    }

}
