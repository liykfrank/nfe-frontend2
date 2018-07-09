package org.iata.bsplink.refund.model.entity;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;

@Getter
public enum FormOfPaymentType {

    @ApiModelProperty(value = "Cash")
    CA(true),

    @ApiModelProperty(value = "Credit")
    CC(false),

    @ApiModelProperty(value = "Easy Pay")
    EP(false),

    @ApiModelProperty(value = "Miscellaneous Credit")
    MSCC(false),

    @ApiModelProperty(value = "Miscellaneous Cash")
    MSCA(true);

    private final boolean isCash;

    private FormOfPaymentType(boolean isCash) {
        this.isCash = isCash;
    }
}
