package org.iata.bsplink.refund.loader.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FormOfPaymentAmount {

    private BigDecimal amount = BigDecimal.ZERO;
    private String number;
    private FormOfPaymentType type;
    private String vendorCode;

}