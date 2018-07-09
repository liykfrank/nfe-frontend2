package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;
import java.util.Objects;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;

public class MixedTaxesValidator extends RefundValueIsEnabledValidator {

    private static final String FIELD_NAME = "amounts.tax";

    public MixedTaxesValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public String getFieldName() {

        return FIELD_NAME;
    }

    @Override
    public boolean isEnabled(Config config) {

        return config.getMixedTaxesAllowed();
    }

    @Override
    protected boolean valueMustBeEnabled(Refund refund) {

        if (refund.getAmounts() == null) {
            return false;
        }

        if (refund.getFormOfPaymentAmounts() == null) {
            return false;
        }

        if (refund.getAmounts().getTax() == null) {
            return false;
        }

        if (refund.getAmounts().getTax().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (refund.getFormOfPaymentAmounts().stream()
            .filter(Objects::nonNull)
            .anyMatch(fop -> fop.getType() == null || fop.getAmount() == null
                || fop.getAmount().compareTo(BigDecimal.ZERO) <= 0)) {
            return false;
        }

        if (refund.getFormOfPaymentAmounts().stream().noneMatch(fop -> fop.getType().isCash())) {
            return false;
        }

        if (refund.getFormOfPaymentAmounts().stream().noneMatch(fop -> !fop.getType().isCash())) {
            return false;
        }

        BigDecimal totalCash = refund.getFormOfPaymentAmounts().stream()
            .filter(fop -> fop.getType().isCash())
            .map(FormOfPaymentAmount::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return refund.getAmounts().getTax().compareTo(totalCash) > 0;
    }

}
