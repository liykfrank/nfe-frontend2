package org.iata.bsplink.refund.loader.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfAirlineAction;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfIssue;
    private Boolean exchange;
    private String isoCountryCode;
    private String issueReason;
    private OriginalIssue originalIssue;
    private String passenger;
    private String rejectionReason;
    private String ticketDocumentNumber;
    private String tourCode;
    @JsonIgnore
    private String transactionNumber;
}
