package org.iata.bsplink.agencymemo.dto;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TaxMiscellaneousFeeRequest {
    public static final String PATTERN = "^(([A-WY-Z][A-Z])|"
            + "([A-Z][A-SU-Z])|"
            + "(O[ABC][A-Z0-9 ./-]{0,6})|"
            + "(XF[A-Z]{3}[0-9]*[1-9][0-9]*(\\.[0-9]+)?)|"
            + "(XF[A-Z]{3}[0-9]+(\\.[0-9]*[1-9][0-9]*)?))$";

    @ApiModelProperty(value = "Tax/Miscellaneous Fee Type", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(min = 2, max = 8, message = INCORRECT_SIZE + "2 - 8")
    @Pattern(regexp = PATTERN, message = INCORRECT_FORMAT)
    private String type;

    @ApiModelProperty(value = "Airline Tax/Miscellaneous Fee Amount", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    private BigDecimal airlineAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "Agent Tax/Miscellaneous Fee Amount", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    private BigDecimal agentAmount = BigDecimal.ZERO;
}
