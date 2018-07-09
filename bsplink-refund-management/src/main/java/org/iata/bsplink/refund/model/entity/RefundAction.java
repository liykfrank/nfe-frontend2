package org.iata.bsplink.refund.model.entity;

import io.swagger.annotations.ApiModelProperty;

public enum RefundAction {

    @ApiModelProperty(value = "Refund issue")
    REFUND_ISSUE,

    @ApiModelProperty(value = "Modify")
    MODIFY,

    @ApiModelProperty(value = "Attach file")
    ATTACH_FILE,

    @ApiModelProperty(value = "Add comment")
    ADD_COMMENT,

    @ApiModelProperty(value = "Refund update via File Massload")
    MASSLOAD_UPDATE,
}
