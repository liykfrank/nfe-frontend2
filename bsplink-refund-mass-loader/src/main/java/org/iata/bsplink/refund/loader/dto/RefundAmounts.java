package org.iata.bsplink.refund.loader.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundAmounts {

    private BigDecimal cancellationPenalty = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
    private BigDecimal commissionOnCpAndMfAmount = BigDecimal.ZERO;
    private BigDecimal commissionOnCpAndMfRate = BigDecimal.ZERO;
    private BigDecimal commissionRate = BigDecimal.ZERO;
    private BigDecimal grossFare = BigDecimal.ZERO;
    private BigDecimal lessGrossFareUsed = BigDecimal.ZERO;
    private BigDecimal miscellaneousFee = BigDecimal.ZERO;
    private BigDecimal refundToPassenger = BigDecimal.ZERO;
    private BigDecimal spam = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal taxOnCancellationPenalty = BigDecimal.ZERO;
    private BigDecimal taxOnMiscellaneousFee = BigDecimal.ZERO;
}
