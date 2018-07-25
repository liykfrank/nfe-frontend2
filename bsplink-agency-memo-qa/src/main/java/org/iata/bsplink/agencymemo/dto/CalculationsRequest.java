package org.iata.bsplink.agencymemo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;


@Data
public class CalculationsRequest {

    @ApiModelProperty(value = "Fare", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal fare = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal tax = BigDecimal.ZERO;

    @ApiModelProperty(value = "Commission", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal commission = BigDecimal.ZERO;

    @ApiModelProperty(value = "SPAM", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal spam = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax on Commission", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal taxOnCommission = BigDecimal.ZERO;
}
