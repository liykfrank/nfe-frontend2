package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;

public class RefundCreditCardEnabledValidator extends RefundValueIsEnabledValidator {

    private static final String FIELD_NAME = "formOfPaymentAmounts";

    public RefundCreditCardEnabledValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public String getFieldName() {

        return FIELD_NAME;
    }

    @Override
    public boolean isEnabled(Config config) {

        return config.getCreditOnIndirectRefundsEnabled();
    }

    @Override
    protected boolean valueMustBeEnabled(Refund refund) {

        if (refund.getFormOfPaymentAmounts() == null) {
            return false;
        }

        return refund.getFormOfPaymentAmounts()
            .stream().anyMatch(x ->
                FormOfPaymentType.CC.equals(x.getType())
                || FormOfPaymentType.MSCC.equals(x.getType()));
    }

    @Override
    public String getDefaultMessage() {

        return "form of payment on credit (CC/MSCC) is not allowed";
    }

}
