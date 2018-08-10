package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@Entity
@ApiModel(description = "ADM / ACM")
@JsonPropertyOrder(alphabetic = true)
public class Acdm {

    @ApiModelProperty(value = "Identifier")
    @Id
    @GeneratedValue
    private Long id;

    @Transient
    @ApiModelProperty(
            value = "Ticket/Document Number",
            readOnly = true)
    private String ticketDocumentNumber;

    @ApiModelProperty(
            value = "Agent Numeric Code, 7 digit agent code + check-digit",
            required = true)
    @Size(min = 8, max = 8)
    @NotNull
    private String agentCode;

    @ApiModelProperty(
            value = "Date of Issue, ISO date format",
            required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dateOfIssue;

    @ApiModelProperty(
            value = "Statistical Code, three letter code",
            required = false)
    @Size(max = 3)
    private String statisticalCode;

    @ApiModelProperty(
            value = "Transaction Code",
            required = true)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(length = 4)
    private TransactionCode transactionCode;

    @ApiModelProperty(
            value = "Ticketing Airline Code Number, three letter code",
            required = true)
    @Size(max = 3)
    @NotNull
    private String airlineCode;

    @ApiModelProperty(
            value = "Passenger Name, pattern: [\\x20-\\x7E]{0,49}",
            required = false)
    @Size(max = 49)
    private String passenger;

    @ApiModelProperty(
            value = "ISO Country Code, two letter code",
            required = true)
    @Size(min = 2, max = 2)
    @NotNull
    private String isoCountryCode;

    @ApiModelProperty(
            value = "Date of Issue Related Document, ISO date format",
            required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfIssueRelatedDocument;

    @Embedded
    @ApiModelProperty(
            value = "Currency",
            required = true)
    @NotNull
    private AcdmCurrency currency = new AcdmCurrency();

    @ApiModelProperty(
           value = "Regularized, there are both positive and negative amount differences",
           required = true)
    @NotNull
    private Boolean regularized = Boolean.FALSE;

    @ApiModelProperty(
            value = "Net-Reporting",
            required = true)
    @NotNull
    private Boolean netReporting = Boolean.FALSE;

    @ApiModelProperty(
            value = "Amount Paid by Customer",
            required = false)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal amountPaidByCustomer = BigDecimal.ZERO;

    @ApiModelProperty(
            value = "Tax on Commission Type, six character code",
            required = false)
    @Size(max = 6)
    private String taxOnCommissionType;

    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(
            value = "Airline Calculations, airline amount data",
            required = true)
    @NotNull
    private Calculations airlineCalculations = new Calculations();

    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(
            value = "Agent Calculations, agent amount data",
            required = true)
    @NotNull
    private Calculations agentCalculations = new Calculations();

    @ApiModelProperty(
            value = "Concerns Indicator",
            required = false)
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private ConcernsIndicator concernsIndicator;

    @ApiModelProperty(
            value = "Reason for Memo Information",
            required = false)
    @Size(max = 45 * 20)
    private String reasonForMemo;

    @ApiModelProperty(
            value = "Reason for Memo Issuance Code",
            required = false)
    @Size(max = 5)
    private String reasonForMemoIssuanceCode;

    @Embedded
    @ApiModelProperty(
            value = "Airline Contact",
            required = true)
    @NotNull
    private Contact airlineContact = new Contact();

    @ApiModelProperty(
            value = "Billing Period, format: yyyyMMp",
            required = true)
    @Min(value = 1000000)
    @Max(value = 9999999)
    @NotNull
    private Integer billingPeriod = 0;

    @ApiModelProperty(
            value = "List of Related Ticket/Document",
            required = false)
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_nr")
    @OrderBy("order_nr")
    private List<RelatedTicketDocument> relatedTicketDocuments = new ArrayList<>();

    @ApiModelProperty(
            value = "Tax/Miscellaneous Fee",
            required = false)
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "order_nr")
    @OrderBy("order_nr")
    private List<TaxMiscellaneousFee> taxMiscellaneousFees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "acdm_id")
    @ApiModelProperty(value = "Attached files", required = false)
    private List<BsplinkFile> attachedFiles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "acdm_id")
    @ApiModelProperty(value = "Comments", required = false)
    private List<Comment> comments;

    @ApiModelProperty(value = "Agent VAT number")
    @Size(max = 30)
    private String agentVatNumber;

    @ApiModelProperty(value = "Airline VAT number")
    @Size(max = 60)
    private String airlineVatNumber;

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

        return (id == null || airlineCode == null) ? null
                : String.format("%s%010d", airlineCode, id);
    }
}
