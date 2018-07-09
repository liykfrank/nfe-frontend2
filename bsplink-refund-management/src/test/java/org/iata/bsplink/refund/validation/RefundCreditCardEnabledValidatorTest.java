package org.iata.bsplink.refund.validation;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.iata.bsplink.refund.test.validation.RefundValueIsEnabledValidatorTestCase;

public class RefundCreditCardEnabledValidatorTest
        extends RefundValueIsEnabledValidatorTestCase<List<FormOfPaymentAmount>> {

    @Override
    protected void setValue(Refund refund, List<FormOfPaymentAmount> value) {

        refund.setFormOfPaymentAmounts(value);;
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setCreditOnIndirectRefundsEnabled(enable);
    }

    @Override
    protected RefundValueIsEnabledValidator getValidator(ConfigService configService) {

        return new RefundCreditCardEnabledValidator(configService);
    }

    @Override
    protected Object[][] validationCases() {

        List<FormOfPaymentAmount> withCc = Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CA),
                getFormOfPayment(FormOfPaymentType.CC));

        List<FormOfPaymentAmount> withMsCc = Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CA),
                getFormOfPayment(FormOfPaymentType.MSCC));

        List<FormOfPaymentAmount> withoutAnyCc = Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CA),
                getFormOfPayment(FormOfPaymentType.EP));

        return new Object[][] {
            { withCc, true, false },
            { withCc, false, true },
            { withMsCc, true, false },
            { withMsCc, false, true },
            { null, true, false },
            { withoutAnyCc, true, false }
        };
    }

    @Override
    protected List<FormOfPaymentAmount> getDefinedValue() {

        return Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CA),
                getFormOfPayment(FormOfPaymentType.CC));
    }

    private FormOfPaymentAmount getFormOfPayment(FormOfPaymentType type) {

        FormOfPaymentAmount formOfPayment = new FormOfPaymentAmount();
        formOfPayment.setType(type);

        return formOfPayment;
    }

}
