package org.iata.bsplink.agencymemo.model.entity;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.iata.bsplink.agencymemo.model.Stat;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Config {

    @Id
    @NonNull
    @Column(nullable = false)
    @Size(min = 2, max = 2)
    private String isoCountryCode;

    @ApiModelProperty(
            value = "The number of related documents that are allowed in an ADM/ACM."
                    + " (Negative value means the number is not limited.)")
    @NotNull(message = NON_NULL_MESSAGE)
    private Integer maxNumberOfRelatedDocuments = 1;

    @ApiModelProperty(
            value = "It is permitted to insert CP tax in ADMs/ACMs which concern an issue.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean cpPermittedForConcerningIssue = false;

    @ApiModelProperty(
            value = "It is permitted to insert MF tax in ADMs/ACMs which concern an issue.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean mfPermittedForConcerningIssue = false;

    @ApiModelProperty(
            value = "It is permitted to insert CP tax in ADMs/ACMs which concern a refund.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean cpPermittedForConcerningRefund = false;

    @ApiModelProperty(
            value = "It is permitted to insert MF tax in ADMs/ACMs which concern a refund.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean mfPermittedForConcerningRefund = false;

    @ApiModelProperty(
            value = "NRID and SPAM can be included in ADM/ACM.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean nridAndSpamEnabled = false;

    @ApiModelProperty(
            value = "Tax on Commission is allowed.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean taxOnCommissionEnabled = true;

    @ApiModelProperty(
            value = "Vat on Commission Positive for Airlines",
            allowableValues = "-1, 1")
    @NotNull(message = NON_NULL_MESSAGE)
    private Integer taxOnCommissionSign = -1;

    @ApiModelProperty(
            value = "Agent VAT Number to be included in an ADM/ACM")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean agentVatNumberEnabled = false;

    @ApiModelProperty(
            value = "Airline VAT Number to be included in an ADM/ACM")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean airlineVatNumberEnabled = false;

    @ApiModelProperty(
            value = "Company Registration Number included in an ADM/ACM.")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean companyRegistrationNumberEnabled = false;

    @ApiModelProperty(
            value = "By default STAT for ADMs/ACMs is defined as International or Domestic.")
    @Enumerated(EnumType.STRING)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 3)
    private Stat defaultStat = Stat.INT;

    @ApiModelProperty(
            value = "Free STAT value (any STAT value is accepted)")
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean freeStat = false;

    @ApiModelProperty(value = "Default Currency")
    @Pattern(regexp = "^[A-Z]{3}$")
    @Size(min = 3, max = 3)
    private String defaultCurrency;
}
