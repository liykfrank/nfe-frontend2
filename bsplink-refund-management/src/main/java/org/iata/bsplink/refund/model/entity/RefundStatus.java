package org.iata.bsplink.refund.model.entity;

public enum RefundStatus {
    DRAFT,
    PENDING,
    PENDING_SUPERVISION,
    UNDER_INVESTIGATION,
    AUTHORIZED,   
    REJECTED,
    RESUBMITTED
}
