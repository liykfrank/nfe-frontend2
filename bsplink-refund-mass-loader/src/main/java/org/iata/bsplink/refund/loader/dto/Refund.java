package org.iata.bsplink.refund.loader.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Refund extends RefundEditable {

    private Long id;
    private Contact agentContact;
    private String agentRegistrationNumber;
    private String agentVatNumber;
    private String airlineCode;
    private Contact airlineContact;
    private String airlineRegistrationNumber;
    private String airlineVatNumber;
    private Integer billingPeriod;
    private LocalDate dateOfAirlineAction;
    private LocalDate dateOfIssue;
    private Boolean exchange;
    private String isoCountryCode;
    private String issueReason;
    private OriginalIssue originalIssue;
    private String rejectionReason;
    private String ticketDocumentNumber;
}
