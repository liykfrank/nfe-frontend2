package org.iata.bsplink.agentmgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

public enum FormOfPaymentType {

    @ApiModelProperty(value = "Credit")
    CC,

    @ApiModelProperty(value = "Cash")
    CA,

    @ApiModelProperty(value = "Easy Pay")
    EP,

    @ApiModelProperty(value = "Miscellaneous Credit")
    MSCC,

    @ApiModelProperty(value = "Miscellaneous Cash")
    MSCA;
}
