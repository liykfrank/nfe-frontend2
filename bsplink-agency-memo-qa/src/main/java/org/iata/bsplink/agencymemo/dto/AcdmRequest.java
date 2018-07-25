package org.iata.bsplink.agencymemo.dto;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.POSITIVE_OR_ZERO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.AgentConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.AirlineConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.CountryConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.CurrencyConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.DecimalsConstraint;
import org.iata.bsplink.agencymemo.validation.constraints.XfTaxesConstraint;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@ApiModel(description = "ADM / ACM")
@AirlineConstraint
@AgentConstraint
@AcdmConstraint
@XfTaxesConstraint
@CurrencyConstraint
@DecimalsConstraint
public class AcdmRequest {

    @ApiModelProperty(
            value = "Ticket/Document Number",
            readOnly = true)
    private String ticketDocumentNumber;

    @ApiModelProperty(
            value = "Agent Numeric Code, 7 digit agent code + check-digit",
            required = true)
    @Size(min = 8, max = 8, message = INCORRECT_SIZE + 8)
    @NotNull(message = NON_NULL_MESSAGE)
    private String agentCode;

    @ApiModelProperty(
            value = "Date of Issue, ISO date format",
            required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = NON_NULL_MESSAGE)
    private LocalDate dateOfIssue;

    @ApiModelProperty(
            value = "Statistical Code, three letter code",
            required = false)
    @Size(max = 3, message = INCORRECT_SIZE + "max 3")
    @Pattern(
            regexp = "^( +)|([ID][ A-Z0-9]{0,2})?$",
            message = INCORRECT_FORMAT)
    private String statisticalCode;

    @ApiModelProperty(
            value = "Transaction Code",
            required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = NON_NULL_MESSAGE)
    private TransactionCode transactionCode;

    @ApiModelProperty(
            value = "Ticketing Airline Code Number, three letter code",
            required = true)
    @Size(max = 3, message = INCORRECT_SIZE + "3")
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(
            regexp = "[A-Z0-9]{3}",
            message = INCORRECT_FORMAT)
    private String airlineCode;

    @ApiModelProperty(
            value = "Passenger Name, pattern: [\\x20-\\x7E]{0,49}",
            required = false)
    @Size(max = 49, message = INCORRECT_SIZE + "max 49")
    @Pattern(regexp = "^[\\x20-\\x7E]*$", message = INCORRECT_FORMAT)
    private String passenger;

    @ApiModelProperty(
            value = "ISO Country Code, two letter code",
            required = true)
    @Size(min = 2, max = 2)
    @Pattern(
            regexp = "[A-Z]*",
            message = INCORRECT_FORMAT)
    @NotNull(message = NON_NULL_MESSAGE)
    @CountryConstraint
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
    @NotNull(message = NON_NULL_MESSAGE)
    private AcdmCurrencyRequest currency = new AcdmCurrencyRequest();

    @ApiModelProperty(
           value = "Regularized, there are both positive and negative amount differences",
           required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean regularized = Boolean.FALSE;

    @ApiModelProperty(
            value = "Net-Reporting",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean netReporting = Boolean.FALSE;

    @ApiModelProperty(
            value = "Amount Paid by Customer",
            required = false)
    @NotNull(message = NON_NULL_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    private BigDecimal amountPaidByCustomer = BigDecimal.ZERO;

    @ApiModelProperty(
            value = "Tax on Commission Type, six character code",
            required = false)
    @Size(max = 6, message = INCORRECT_SIZE + "max 6")
    private String taxOnCommissionType;

    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(
            value = "Airline Calculations, airline amount data",
            required = true)
    @Valid
    @NotNull(message = NON_NULL_MESSAGE)
    private CalculationsRequest airlineCalculations = new CalculationsRequest();

    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(
            value = "Agent Calculations, agent amount data",
            required = true)
    @Valid
    @NotNull(message = NON_NULL_MESSAGE)
    private CalculationsRequest agentCalculations = new CalculationsRequest();

    @ApiModelProperty(
            value = "Concerns Indicator",
            required = false)
    @Enumerated(EnumType.STRING)
    private ConcernsIndicator concernsIndicator;

    @ApiModelProperty(
            value = "Reason for Memo Information",
            required = false)
    @Size(max = 45 * 20, message = INCORRECT_SIZE + (45 * 20))
    private String reasonForMemo;

    @ApiModelProperty(
            value = "Reason for Memo Issuance Code",
            required = false)
    @Size(max = 5, message = INCORRECT_SIZE)
    @Pattern(regexp = "^[A-Z0-9 ./-]*$", message = INCORRECT_FORMAT)
    private String reasonForMemoIssuanceCode;

    @Embedded
    @ApiModelProperty(
            value = "Airline Contact",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Valid
    private ContactRequest airlineContact = new ContactRequest();

    @ApiModelProperty(
            value = "Billing Period, format: yyyyMMp",
            required = true)
    @Min(value = 1000000, message = INCORRECT_FORMAT)
    @Max(value = 9999999, message = INCORRECT_FORMAT)
    @NotNull(message = NON_NULL_MESSAGE)
    private Integer billingPeriod = 0;

    @ApiModelProperty(
            value = "List of Related Ticket/Document",
            required = false)
    @Valid
    private List<RelatedTicketDocumentRequest> relatedTicketDocuments = new ArrayList<>();

    @ApiModelProperty(
            value = "Tax/Miscellaneous Fee",
            required = false)
    @Valid
    private List<TaxMiscellaneousFeeRequest> taxMiscellaneousFees = new ArrayList<>();

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
}
