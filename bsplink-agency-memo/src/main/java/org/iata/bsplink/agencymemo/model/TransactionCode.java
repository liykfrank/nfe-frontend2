package org.iata.bsplink.agencymemo.model;

import lombok.Getter;

@Getter
public enum TransactionCode {
    ACMA(false),
    SPCR(false),
    ACMD(false),

    ADMA(true),
    SPDR(true),
    ADMD(true);

    private boolean adm;

    private TransactionCode(boolean isAdm) {
        this.adm = isAdm;
    }
}
