package org.iata.bsplink.agentmgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

public enum LocationType {
    @ApiModelProperty(value = "Head Entity")
    HE,

    @ApiModelProperty(value = "Associate Entity")
    AE
}
