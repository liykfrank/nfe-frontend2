package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.refund.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.refund.validation.constraints.FormOfPaymentConsistentDataConstraint;

@Data
@Embeddable
@FormOfPaymentConsistentDataConstraint
public class FormOfPaymentAmount {

    @ApiModelProperty(value = "Form of Payment Type", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    private FormOfPaymentType type;

    @ApiModelProperty(value = "Form of Payment Amount", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    @Digits(integer = 11, fraction = 9)
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty(value = "Credit Card Vendor Code")
    @Size(min = 2, max = 2, message = INCORRECT_SIZE + 2)
    @Column(length = 2)
    private String vendorCode;

    @ApiModelProperty(value = "Credit Card Number")
    @Size(max = 20, message = INCORRECT_SIZE + "max 20")
    @Column(length = 20)
    private String number;

}