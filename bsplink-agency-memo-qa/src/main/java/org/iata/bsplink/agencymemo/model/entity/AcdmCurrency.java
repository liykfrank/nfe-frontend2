package org.iata.bsplink.agencymemo.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class AcdmCurrency {
    @ApiModelProperty(
            value = "Currency Code, three letter code",
            required = true)
    @Size(min = 3, max = 3)
    @NotNull
    @Column(name = "currency_code", length = 3)
    private String code;

    @ApiModelProperty(
            value = "Currency Decimals",
            required = true,
            allowableValues = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9")
    @PositiveOrZero
    @Max(value = 9)
    @NotNull
    @Column(name = "currency_decimals")
    private Integer decimals;
}
