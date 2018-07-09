package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.refund.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;

@Data
@Embeddable
public class RefundAmounts {

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal grossFare = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal lessGrossFareUsed = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 4, scale = 2)
    private BigDecimal commissionRate = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal spam = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal cancellationPenalty = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal miscellaneousFee = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal taxOnCancellationPenalty = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal taxOnMiscellaneousFee = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal commissionOnCpAndMfAmount = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 4, scale = 2)
    private BigDecimal commissionOnCpAndMfRate = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal tax = BigDecimal.ZERO;

    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal refundToPassenger = BigDecimal.ZERO;
}
