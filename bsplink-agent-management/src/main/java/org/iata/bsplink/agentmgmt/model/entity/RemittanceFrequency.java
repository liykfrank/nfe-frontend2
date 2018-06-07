package org.iata.bsplink.agentmgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

public enum RemittanceFrequency {
    @ApiModelProperty(value = "Weekly")
    W,
    @ApiModelProperty(value = "Twice a Week")
    T,
    @ApiModelProperty(value = "Super Weekly")
    S,
    @ApiModelProperty(value = "Fortnightly")
    F,
    @ApiModelProperty(value = "Every 10 days (dual)")
    D,
    @ApiModelProperty(value = "Monthly")
    M,
    @ApiModelProperty(value = "Every 10 days")
    R
}
