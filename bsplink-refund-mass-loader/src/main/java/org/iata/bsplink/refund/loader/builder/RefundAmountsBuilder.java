package org.iata.bsplink.refund.loader.builder;

import static org.iata.bsplink.refund.loader.utils.MathUtils.applyDecimals;

import java.math.BigDecimal;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RefundAmountsBuilder {
    private RecordIt05 it05;
    private Integer numDecimals;
    private BigDecimal cancellationPenalty;
    private BigDecimal miscellaneousFee;
    private BigDecimal tax;

    /**
     * Creates RefundAmounts.
     * @return the created RefundAmounts.
     */
    public RefundAmounts build() {
        RefundAmounts amounts = new RefundAmounts();

        amounts.setCommissionAmount(applyDecimals(it05.getCommissionAmount1(), numDecimals));
        if (amounts.getCommissionAmount().signum() == 0) {
            amounts.setCommissionRate(applyDecimals(it05.getCommissionRate1(), 2));
        }

        assignSpam(amounts);
        amounts.setLessGrossFareUsed(BigDecimal.ZERO);
        assignGrossFare(amounts);
        amounts.setCancellationPenalty(cancellationPenalty);
        amounts.setMiscellaneousFee(miscellaneousFee);
        amounts.setTax(tax);
        assignCommissionOnCoAndMf(amounts);
        assignRefundToPassenger(amounts);

        return amounts;
    }



    private void assignRefundToPassenger(RefundAmounts amounts) {
        if (amounts.getGrossFare() == null || amounts.getTax() == null
                || amounts.getCancellationPenalty() == null
                || amounts.getMiscellaneousFee() == null) {
            amounts.setRefundToPassenger(null);
        } else {
            amounts.setRefundToPassenger(amounts.getGrossFare().add(amounts.getTax())
                    .subtract(amounts.getCancellationPenalty())
                    .subtract(amounts.getMiscellaneousFee()));
        }
    }


    private void assignCommissionOnCoAndMf(RefundAmounts amounts) {
        String xlpAmount;
        String xlpRate;
        if ("XLP".equals(it05.getCommissionType2())) {
            xlpAmount = it05.getCommissionAmount2();
            xlpRate = it05.getCommissionRate2();
        } else if ("XLP".equals(it05.getCommissionType3())) {
            xlpAmount = it05.getCommissionAmount3();
            xlpRate = it05.getCommissionRate3();
        } else {
            return;
        }

        if (xlpAmount.matches("^0+$")) {
            amounts.setCommissionOnCpAndMfRate(applyDecimals(xlpRate, 2));
        } else {
            amounts.setCommissionOnCpAndMfRate(applyDecimals(xlpAmount, numDecimals));
        }
    }


    private void assignGrossFare(RefundAmounts amounts) {
        BigDecimal tdam = applyDecimals(it05.getTicketDocumentAmount(), numDecimals);
        if (tdam == null) {
            amounts.setGrossFare(null);
        } else {
            BigDecimal grossFare = tdam;
            if (amounts.getTax() != null) {
                grossFare = grossFare.subtract(amounts.getTax());
            }
            if (amounts.getCancellationPenalty() != null) {
                grossFare = grossFare.add(amounts.getCancellationPenalty());
            }
            if (amounts.getMiscellaneousFee() != null) {
                grossFare = grossFare.add(amounts.getMiscellaneousFee());
            }
            amounts.setGrossFare(grossFare);
        }
    }


    private void assignSpam(RefundAmounts amounts) {
        if (StringUtils.isBlank(it05.getCommissionType2())
                && it05.getNetReportingIndicator().equals("NR")) {
            amounts.setSpam(applyDecimals(it05.getCommissionAmount2(), numDecimals));
        }
    }
}
