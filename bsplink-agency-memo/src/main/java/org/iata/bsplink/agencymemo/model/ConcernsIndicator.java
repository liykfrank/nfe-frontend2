package org.iata.bsplink.agencymemo.model;

import io.swagger.annotations.ApiModelProperty;

public enum ConcernsIndicator {
    @ApiModelProperty(value = "Issue")
    I,

    @ApiModelProperty(value = "Refund")
    R,

    @ApiModelProperty(value = "Exchange")
    X,

    @ApiModelProperty(value = "Emd")
    E
}
