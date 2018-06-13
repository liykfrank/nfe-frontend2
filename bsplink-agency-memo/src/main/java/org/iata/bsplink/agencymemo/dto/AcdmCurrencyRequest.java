package org.iata.bsplink.agencymemo.dto;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AcdmCurrencyRequest {
    @ApiModelProperty(
            value = "Currency Code, three letter code",
            required = true)
    @Size(
            min = 3, max = 3,
            message = INCORRECT_SIZE + 3)
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(
            regexp = "[A-Z]*",
            message = INCORRECT_FORMAT)
    private String code;

    @ApiModelProperty(
            value = "Currency Decimals",
            required = true,
            allowableValues = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9")
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    @Max(
            value = 9,
            message = "Max value is 9")
    @NotNull(message = NON_NULL_MESSAGE)
    private Integer decimals;
}
