package org.iata.bsplink.refund.loader.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "id")
public class Refund {

    private Long id;
    private String ticketDocumentNumber;
    private String isoCountryCode;
    private String airlineCode;
}
