package org.iata.bsplink.refund.validation;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.iata.bsplink.refund.test.validation.RefundValueIsEnabledValidatorTestCase;

public class RefundEasyPayEnabledValidatorTest
        extends RefundValueIsEnabledValidatorTestCase<List<FormOfPaymentAmount>> {

    @Override
    protected void setValue(Refund refund, List<FormOfPaymentAmount> value) {

        refund.setFormOfPaymentAmounts(value);;
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setEasyPayEnabled(enable);
    }

    @Override
    protected RefundValueIsEnabledValidator getValidator(ConfigService configService) {

        return new RefundEasyPayEnabledValidator(configService);
    }

    @Override
    protected Object[][] validationCases() {

        List<FormOfPaymentAmount> withEasyPay = getDefinedValue();

        List<FormOfPaymentAmount> withoutEasyPay = Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CC),
                getFormOfPayment(FormOfPaymentType.CA));

        return new Object[][] {
            { withEasyPay, true, false },
            { null, true, false },
            { withoutEasyPay, true, false },
            { withEasyPay, false, true }
        };
    }

    @Override
    protected List<FormOfPaymentAmount> getDefinedValue() {

        return Arrays.asList(
                getFormOfPayment(FormOfPaymentType.CC),
                getFormOfPayment(FormOfPaymentType.EP));
    }

    private FormOfPaymentAmount getFormOfPayment(FormOfPaymentType type) {

        FormOfPaymentAmount formOfPayment = new FormOfPaymentAmount();
        formOfPayment.setType(type);

        return formOfPayment;
    }

}
