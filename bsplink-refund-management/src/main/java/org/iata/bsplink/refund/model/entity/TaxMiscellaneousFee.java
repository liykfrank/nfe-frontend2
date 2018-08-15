package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.refund.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class TaxMiscellaneousFee {
    public static final String PATTERN = "^(([A-WY-Z][A-Z])|"
            + "([A-Z][A-SU-Z])|"
            + "(O[ABC][A-Z0-9 ./-]{0,6})|"
            + "(XF[A-Z]{3}[0-9]*[1-9][0-9]*(\\.[0-9]+)?)|"
            + "(XF[A-Z]{3}[0-9]+(\\.[0-9]*[1-9][0-9]*)?))$";

    @ApiModelProperty(value = "Tax/Miscellaneous Fee Type", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(min = 2, max = 8, message = INCORRECT_SIZE + "min 2, max 8")
    @Column(length = 8)
    @Pattern(regexp = PATTERN, message = INCORRECT_FORMAT)
    private String type;

    @ApiModelProperty(value = "Tax/Miscellaneous Fee Amount", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Column(precision = 20, scale = 9)
    @Digits(integer = 11, fraction = 9)
    private BigDecimal amount = BigDecimal.ZERO;
}
