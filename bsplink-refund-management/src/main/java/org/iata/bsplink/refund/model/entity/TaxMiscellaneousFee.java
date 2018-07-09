package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.refund.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class TaxMiscellaneousFee {
    @ApiModelProperty(value = "Tax/Miscellaneous Fee Type", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(min = 2, max = 8, message = INCORRECT_SIZE + "min 2, max 8")
    @Column(length = 8)
    private String type;

    @ApiModelProperty(value = "Tax/Miscellaneous Fee Amount", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    private BigDecimal amount = BigDecimal.ZERO;
}
