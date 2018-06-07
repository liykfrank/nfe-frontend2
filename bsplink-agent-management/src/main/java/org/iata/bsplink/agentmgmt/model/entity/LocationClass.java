package org.iata.bsplink.agentmgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

public enum LocationClass {
    @ApiModelProperty(value = "AIRLINE POINTS OF SALE")
    A,
    @ApiModelProperty(value = "CARGO")
    C,
    @ApiModelProperty(value = "DOMESTIC")
    D,
    @ApiModelProperty(value = "ERSP (ELEC.RSVTN.SRV.PROVIDER)")
    F,
    @ApiModelProperty(value = "GSA")
    G,
    @ApiModelProperty(value = "IMPORT AGENTS")
    I,
    @ApiModelProperty(value = "CATO")
    K,
    @ApiModelProperty(value = "NON-IATA LOCATIONS")
    L,
    @ApiModelProperty(value = "MSO")
    M,
    @ApiModelProperty(value = "NISI")
    N,
    @ApiModelProperty(value = "SATO")
    O,
    @ApiModelProperty(value = "PASSENGER")
    P,
    @ApiModelProperty(value = "COURIERS")
    Q,
    @ApiModelProperty(value = "CASS ASSOCIATES")
    R,
    @ApiModelProperty(value = "SSI")
    S,
    @ApiModelProperty(value = "TIDS")
    T,
    @ApiModelProperty(value = "PUERTO RICO AND US VIRGIN IS.")
    U,
    @ApiModelProperty(value = "HANDLING AGTS & SHIPPING LINES")
    X,
    @ApiModelProperty(value = "ASSOCIATIONS")
    Y,
    @ApiModelProperty(value = "TRAVEL IND SUPPLIERS")
    Z
}
