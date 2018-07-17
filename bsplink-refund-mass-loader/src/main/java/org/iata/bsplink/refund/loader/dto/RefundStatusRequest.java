package org.iata.bsplink.refund.loader.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundStatusRequest {

    private RefundStatus status;
    private String airlineRemark;
    private String rejectionReason;
}
