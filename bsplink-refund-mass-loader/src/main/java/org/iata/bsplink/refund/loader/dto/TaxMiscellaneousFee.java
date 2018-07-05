package org.iata.bsplink.refund.loader.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxMiscellaneousFee {

    private BigDecimal amount = BigDecimal.ZERO;
    private String type;
}
