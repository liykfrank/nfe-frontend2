package org.iata.bsplink.refund.loader.builder;

import lombok.Setter;

import org.iata.bsplink.refund.loader.dto.RefundCurrency;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.utils.MathUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RefundCurrencyBuilder {
    private RecordIt05 it05;

    /**
     * Creates RefundCurrency.
     * @return The created RefundCurrency.
     */
    public RefundCurrency build() {
        String cutp = it05.getCurrencyType();
        RefundCurrency refundCurrency = new RefundCurrency();
        if (cutp.length() > 2) {
            refundCurrency.setCode(cutp.substring(0, 3));
        }
        if (cutp.length() > 3) {
            refundCurrency.setDecimals(MathUtils.parseInt(cutp.substring(3)));
        }
        return refundCurrency;
    }
}
