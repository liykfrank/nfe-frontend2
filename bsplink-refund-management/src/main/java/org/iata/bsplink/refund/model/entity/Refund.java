package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.iata.bsplink.refund.validation.constraints.DateOfIssueRelatedDocumentConstraint;
import org.iata.bsplink.refund.validation.constraints.DecimalsConstraint;
import org.iata.bsplink.refund.validation.constraints.FormOfPaymentAmountsConstraint;
import org.iata.bsplink.refund.validation.constraints.NetReportingConstraint;
import org.iata.bsplink.refund.validation.constraints.OriginalIssueConstraint;
import org.iata.bsplink.refund.validation.constraints.RefundAmountsConstraint;
import org.iata.bsplink.refund.validation.constraints.TaxAmountsConstraint;
import org.iata.bsplink.refund.validation.constraints.XfTaxesConstraint;

@Data
@EqualsAndHashCode(exclude = {"id", "ticketDocumentNumber"})
@NoArgsConstructor
@Entity
@ApiModel(description = "Refund")
@JsonPropertyOrder(alphabetic = true)
@FormOfPaymentAmountsConstraint
@XfTaxesConstraint
@TaxAmountsConstraint
@DecimalsConstraint
@OriginalIssueConstraint
@DateOfIssueRelatedDocumentConstraint
@NetReportingConstraint
public class Refund {

    @ApiModelProperty(value = "Identifier")
    @Id
    @GeneratedValue
    private Long id;

    @Transient
    @ApiModelProperty(value = "Ticket/Document Number", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ticketDocumentNumber;

    @ApiModelProperty(value = "Refund Status")
    @Enumerated(EnumType.STRING)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 19)
    private RefundStatus status;

    @ApiModelProperty(value = "ISO Country Code, two letter code", required = true)
    @Size(min = 2, max = 2, message = INCORRECT_SIZE + 2)
    @NotNull(message = NON_NULL_MESSAGE)
    private String isoCountryCode;

    @ApiModelProperty(value = "Agent Code", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(min = 8, max = 8, message = INCORRECT_SIZE + 8)
    private String agentCode;

    @ApiModelProperty(value = "Airline Code")
    @Size(min = 3, max = 3, message = INCORRECT_SIZE + 3)
    private String airlineCode;

    @ApiModelProperty(value = "Date of Issue of the refund", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private LocalDate dateOfIssue;

    @ApiModelProperty(value = "Billing Period, format: yyyyMMp")
    @Min(value = 1000000)
    @Max(value = 9999999)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer billingPeriod;

    @ApiModelProperty(value = "Currency", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Valid
    private RefundCurrency currency = new RefundCurrency();

    @ApiModelProperty(value = "Passenger")
    @Size(max = 49, message = INCORRECT_SIZE + "max 49")
    @Pattern(regexp = "^[\\x20\\x2D-\\x39\\x41-\\x5A\\x61-\\x7A]*$", message = INCORRECT_FORMAT)
    private String passenger;

    @ApiModelProperty(value = "Amounts", required = true)
    @Valid
    @RefundAmountsConstraint
    @NotNull(message = NON_NULL_MESSAGE)
    private RefundAmounts amounts = new RefundAmounts();

    @ApiModelProperty(value = "Partial Refund", required = false)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean partialRefund = Boolean.FALSE;

    @ApiModelProperty(value = "Net Reporting", required = false)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean netReporting = Boolean.FALSE;

    @ApiModelProperty(value = "Waiver Code", required = false)
    @Size(max = 14, message = INCORRECT_SIZE + "max 14")

    @Pattern(regexp = "^[A-Za-z0-9 /.-]*$", message = INCORRECT_FORMAT)
    private String waiverCode;

    @ApiModelProperty(value = "Tour Code", required = false)
    @Pattern(regexp = "^[\\x20\\x21\\x23-\\x7E]*", message = INCORRECT_FORMAT)
    @Size(max = 15, message = INCORRECT_SIZE + "max 15")
    private String tourCode;

    @ApiModelProperty(value = "Date of Issue of the refunded document")
    private LocalDate dateOfIssueRelatedDocument;

    @ApiModelProperty(value = "Date of Authorisation/ Rejection")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dateOfAirlineAction;

    @ApiModelProperty(value = "Exchange", required = true)
    private Boolean exchange = Boolean.FALSE;

    @ApiModelProperty(value = "Agent Contact", required = false)
    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private Contact agentContact;

    @ApiModelProperty(value = "Airline Contact", required = false)
    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private Contact airlineContact;

    @ApiModelProperty(value = "Electronic Ticket Authorisation Code", required = false)
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = INCORRECT_FORMAT)
    @Size(max = 14, message = INCORRECT_SIZE + "max 14")
    private String settlementAuthorisationCode;

    @ApiModelProperty(value = "Customer Reference", required = false)
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = INCORRECT_FORMAT)
    @Size(max = 27, message = INCORRECT_SIZE + "max 27")
    private String customerFileReference;

    @ApiModelProperty(value = "Agent VAT number")
    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    private String agentVatNumber;

    @ApiModelProperty(value = "Airline VAT number")
    @Size(max = 60, message = INCORRECT_SIZE + "max 60")
    private String airlineVatNumber;

    @Embedded
    @ApiModelProperty(value = "Airline Contact", required = false)
    @Valid
    private OriginalIssue originalIssue;

    @ApiModelProperty(value = "Airline Code of the refunded document")
    @Size(min = 3, max = 3, message = INCORRECT_SIZE + "3")
    private String airlineCodeRelatedDocument;

    @Embedded
    @ApiModelProperty(value = "Refunded Document", required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Valid
    private RelatedDocument relatedDocument;

    @ApiModelProperty(value = "Statistical Code, three letter code", required = false)
    @Size(max = 3, message = INCORRECT_SIZE + "max 3")
    @Pattern(regexp = "^( +)|([ID][ A-Z0-9]{0,2})?$", message = INCORRECT_FORMAT)
    @NotNull(message = NON_NULL_MESSAGE)
    private String statisticalCode;

    @ApiModelProperty(value = "Form Of Payments", required = true)
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_nr")
    @OrderBy("order_nr")
    @Valid
    private List<FormOfPaymentAmount> formOfPaymentAmounts = new ArrayList<>();

    @ApiModelProperty(value = "Conjunctions of the refunded document", required = false)
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_nr")
    @OrderBy("order_nr")
    @NotNull(message = NON_NULL_MESSAGE)
    @Valid
    private List<RelatedDocument> conjunctions = new ArrayList<>();

    @ApiModelProperty(value = "List of Tax/Miscellaneous Fees", required = false)
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_nr")
    @OrderBy("order_nr")
    @Valid
    private List<TaxMiscellaneousFee> taxMiscellaneousFees = new ArrayList<>();

    @ApiModelProperty(value = "Reason for Issuance")
    @Size(max = 500, message = INCORRECT_SIZE + "max 500")
    @Valid
    private String issueReason;

    @ApiModelProperty(value = "Rejection Reason")
    @Size(max = 500, message = INCORRECT_SIZE + "max 500")
    @Valid
    private String rejectionReason;

    @ApiModelProperty(value = "Airline Remark")
    @Size(max = 500, message = INCORRECT_SIZE + "max 500")
    @Valid
    private String airlineRemark;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id")
    @ApiModelProperty(value = "Attached files", required = false)
    private List<BsplinkFile> attachedFiles;


    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id")
    @ApiModelProperty(value = "Comments", required = false)
    private List<Comment> comments;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id")
    @ApiModelProperty(value = "History", required = false)
    private List<RefundHistory> history;

    @ApiModelProperty(value = "Agent registration number")
    @Size(max = 20)
    private String agentRegistrationNumber;

    @ApiModelProperty(value = "Airline registration number")
    @Size(max = 20)
    private String airlineRegistrationNumber;

    /**
     * Creates a faked ticketDocumentNumber.
     */
    public String getTicketDocumentNumber() {

        return id == null ? null : String.format("%010d", id);
    }
}
