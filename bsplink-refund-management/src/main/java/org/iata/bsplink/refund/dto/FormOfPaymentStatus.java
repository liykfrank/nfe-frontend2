package org.iata.bsplink.refund.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormOfPaymentStatus {

    @JsonProperty(value = "Active")
    ACTIVE,

    @JsonProperty(value = "Non-Active")
    NON_ACTIVE,

    @JsonProperty(value = "Not Authorized")
    NOT_AUTHORIZED
}
