package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_FORMAT;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.iata.bsplink.refund.model.Stat;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Config {

    @Id
    @NonNull
    @Size(min = 2, max = 2)
    private String isoCountryCode;

    @ApiModelProperty(value = "Agent VAT number to be included in refund")
    @NotNull
    private Boolean agentVatNumberEnabled = false;

    @ApiModelProperty(value = "Airline VAT number to be included in refund")
    @NotNull
    private Boolean airlineVatNumberEnabled = false;

    @ApiModelProperty(value = "Company registration number included in refund")
    @NotNull
    private Boolean companyRegistrationNumberEnabled = false;

    @ApiModelProperty(
            value = "By default STAT for refunds is defined as international or domestic")
    @Enumerated(EnumType.STRING)
    @Column(length = 3)
    @NotNull
    private Stat defaultStat = Stat.INT;

    @ApiModelProperty(value = "Whether easy pay is enabled or not")
    @NotNull
    private Boolean easyPayEnabled = false;

    @ApiModelProperty(value = "Free STAT value (any STAT value is accepted)")
    @NotNull
    private Boolean freeStat = false;

    @ApiModelProperty(value = "Whether it is possible to issue credit indirect refunds")
    @NotNull
    private Boolean creditOnIndirectRefundsEnabled = false;

    @ApiModelProperty(value = "Whether it is possible to issue credit direct refunds")
    @NotNull
    private Boolean creditOnDirectRefundsEnabled = false;

    @ApiModelProperty(value = "If it is discount ammount permitted or not")
    @NotNull
    private Boolean fareAdjustmentAmountEnabled = false;

    @ApiModelProperty(value =
            "Standard MF Fee for Refund Applications issued in the Default Currency.")
    @NotNull
    @Column(precision = 20, scale = 9)
    @Digits(integer = 11, fraction = 9)
    @PositiveOrZero
    private BigDecimal mfAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "Whether enter the value of MF Tax is enabled or not")
    @NotNull
    private Boolean handlingFeeEnabled = false;

    @ApiModelProperty(value = "Whether enter the value of CP Tax is enabled or not")
    @NotNull
    private Boolean penaltyChargeEnabled = false;

    @ApiModelProperty(value =
            "Whether enter the value of Commission on CP and MF is enabled or not")
    @NotNull
    private Boolean commissionOnCpAndMfEnabled = false;

    @ApiModelProperty(value = "If it is permitted to mix taxes in refunds")
    @NotNull
    private Boolean mixedTaxesAllowed = false;

    @ApiModelProperty(value = "If NR refunds are allowed")
    @NotNull
    private Boolean nrRefundsAllowed = false;

    @ApiModelProperty(value = "Whether to allow the handling fee on refunds, penalty charge on "
            + "refunds and commission on CP and MF parameters to be managed by the airlines")
    @NotNull
    private Boolean airlineMfAndCpConfigurationAllowed = false;

    @ApiModelProperty(value = "If electronic tickect validations are enabled")
    @NotNull
    private Boolean electronicTicketValidationsEnabled = false;

    @ApiModelProperty(value = "Consider refund notice number when issuing "
            + "(on-line or via Mass Loader) and querying these documents")
    @NotNull
    private Boolean refundNoticeNumberConsidered = false;

    @ApiModelProperty(value = "TCTP for VAT on CP")
    @Size(max = 6)
    private String tctpForVatOnCp;

    @ApiModelProperty(value = "TCTP for VAT on MF")
    @Size(max = 6)
    private String tctpForVatOnMf;

    @ApiModelProperty(value = "Enabled VAT on MF and CP fields in Refund "
            + "Application / Authorisation. Only makes sense in case of MF "
            + "or CP permissions in refunds issues.")
    @NotNull
    private Boolean vatOnMfAndVatOnCpEnabled = false;

    @ApiModelProperty(value = "Whether issue refunds without coupons is allowed or not")
    @NotNull
    private Boolean issueRefundsWithoutCouponsAllowed = false;

    @ApiModelProperty(value = "Agents can use BSPlink to issue refund notice, although GDS offers"
            + "them such possibility")
    @NotNull
    private Boolean allGdsAgentsIssueRefundNoticeAllowed = false;

    @ApiModelProperty(value = "Default Currency")
    @Pattern(regexp = "^[A-Z]{3}$", message = INCORRECT_FORMAT)
    @Size(min = 3, max = 3)
    private String defaultCurrency;

    @ApiModelProperty(value = "Maximum number of coupons used in all related documents "
            + "(default 16)")
    @NotNull
    private Integer maxCouponsInRelatedDocuments = 16;

    @ApiModelProperty(value = "Maximum number of conjunctions")
    @NotNull
    private Integer maxConjunctions = 5;
}
