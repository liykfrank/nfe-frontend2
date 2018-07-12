package org.iata.bsplink.refund.loader.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundCurrency {

    private String code;
    private Integer decimals;
}
