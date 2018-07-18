package org.iata.bsplink.refund.loader.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefundStatusRequest {

    private RefundStatus status;
    private String airlineRemark;
    private String rejectionReason;
}
