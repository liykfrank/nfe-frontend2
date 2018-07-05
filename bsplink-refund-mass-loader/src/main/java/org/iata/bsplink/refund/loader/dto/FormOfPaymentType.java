package org.iata.bsplink.refund.loader.dto;

import lombok.Getter;

@Getter
public enum FormOfPaymentType {

    CA(true),
    CC(false),
    EP(false),
    MSCA(true),
    MSCC(false);

    private final boolean isCash;

    private FormOfPaymentType(boolean isCash) {
        this.isCash = isCash;
    }
}
