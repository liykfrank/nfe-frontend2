package org.iata.bsplink.refund.loader.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OriginalIssue {

    private String originalAgentCode;
    private String originalAirlineCode;
    private LocalDate originalDateOfIssue;
    private String originalLocationCityCode;
    private String originalTicketDocumentNumber;
}
