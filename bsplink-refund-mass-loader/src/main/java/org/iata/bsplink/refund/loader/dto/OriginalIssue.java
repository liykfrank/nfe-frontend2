package org.iata.bsplink.refund.loader.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OriginalIssue {

    private String originalAgentCode;
    private String originalAirlineCode;
    private LocalDate originalDateOfIssue;
    private String originalLocationCityCode;
    private String originalTicketDocumentNumber;
}
