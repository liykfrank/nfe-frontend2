package org.iata.bsplink.refund.fake;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Currency {

    private String name;
    private Integer numDecimals;
    private LocalDate expirationDate;
}
