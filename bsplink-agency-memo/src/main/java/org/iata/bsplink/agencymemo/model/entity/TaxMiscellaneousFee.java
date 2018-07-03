package org.iata.bsplink.agencymemo.model.entity;

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
    @NotNull
    @Size(min = 2, max = 8)
    @Column(length = 8)
    private String type;

    @ApiModelProperty(value = "Airline Tax/Miscellaneous Fee Amount", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal airlineAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "Agent Tax/Miscellaneous Fee Amount", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal agentAmount = BigDecimal.ZERO;
}
