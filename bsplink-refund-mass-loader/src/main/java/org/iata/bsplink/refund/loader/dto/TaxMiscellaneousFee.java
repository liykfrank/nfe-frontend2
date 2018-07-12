package org.iata.bsplink.refund.loader.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaxMiscellaneousFee {

    private BigDecimal amount = BigDecimal.ZERO;
    private String type;
}
