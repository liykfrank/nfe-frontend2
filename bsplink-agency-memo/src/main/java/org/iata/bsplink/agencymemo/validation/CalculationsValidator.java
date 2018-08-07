package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.utils.CalculationsUtility.calculationsDifference;
import static org.iata.bsplink.agencymemo.utils.CalculationsUtility.isToRegularize;
import static org.iata.bsplink.agencymemo.utils.CalculationsUtility.isZero;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CalculationsRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.model.entity.Calculations;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;

public class CalculationsValidator
        implements ConstraintValidator<AcdmConstraint, AcdmRequest> {

    public static final String INCORRECT_TOTAL_MSG = "The Total Amount is incorrect.";
    public static final String INCORRECT_TAX_SUM_MSG = "The Tax Total is incorrect.";
    public static final String NO_NR_PERMITTED_MSG = "NetReporting is not permitted.";
    public static final String NO_SPAM_PERMITTED_MSG = "SPAM is not permitted.";
    public static final String SPAM_ONLY_IN_NR_MSG = "SPAM only permitted in Net Reporting.";
    public static final String TOCA_NOT_PERMITTED_MSG = "Tax on Commission is not permitted";
    public static final String INCORRECT_TOTAL_ADM_ISSUE_MSG =
            "The remittance data is inconsistent for an ADM concerning an issue.";
    public static final String INCORRECT_TOTAL_ACM_RFND_MSG =
            "The remittance data is inconsistent for an ACM concerning a refund.";
    public static final String INCORRECT_TOTAL_ACM_ISSUE_MSG =
            "The remittance data is inconsistent for an ACM concerning an issue.";
    public static final String INCORRECT_TOTAL_ADM_RFND_MSG =
            "The remittance data is inconsistent for an ADM concerning a refund.";
    public static final String REGULARIZED_MSG =
            "You reported both positive and negative amounts in the differences, therefore,"
            + " only the total net difference will be reported to the DPC and will be"
            + " included in the net billing.";
    public static final String NO_REGULARIZED_MSG =
            "You have not reported both positive and negative amounts in the differences.";
    public static final String TCTP_MISSING_MSG =
            "Tax on Commission Type for the reported Tax on Commission Amount is missing.";
    public static final String TCTP_NOT_PERMITTED_MSG =
            "Tax on Commission Type only to be reported if there is a Tax on Commission Amount";
    public static final String ACDMD_FARE_MISSING_MSG =
            "For an ACMD/ ADMD a Fare Amount has to be reported.";
    public static final String ACDMD_NO_AMOUNT_PERMITTED_MSG =
            "For an ACMD/ ADMD the Amount is not permitted.";

    private ConfigService configService;

    public CalculationsValidator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean isValid(AcdmRequest acdm,
            ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (acdm.getIsoCountryCode() == null) {
            return true;
        }
        if (acdm.getTransactionCode() == null) {
            return true;
        }
        if (calculationsHasNullValue(acdm.getAgentCalculations())) {
            return true;
        }
        if (calculationsHasNullValue(acdm.getAirlineCalculations())) {
            return true;
        }

        Config cfg = configService.find(acdm.getIsoCountryCode());

        if (!netReportingCheck(cfg, acdm, context)) {
            return false;
        }
        if (!isValidToca(cfg, acdm, context)) {
            return false;
        }
        if ((acdm.getTransactionCode().equals(TransactionCode.ACMD)
                || acdm.getTransactionCode().equals(TransactionCode.ADMD))
                && !isValidAcdmd(acdm, context)) {
            return false;
        }
        if (!isValidTotal(cfg, acdm, context)) {
            return false;
        }

        boolean result = true;

        result = isValidRegularized(cfg, acdm, context) && result;

        if (acdm.getTaxMiscellaneousFees() != null) {
            result = totalTaxCheck(acdm, context) && result;
        }

        return result;
    }


    private boolean isValidAcdmd(AcdmRequest acdm, ConstraintValidatorContext context) {
        boolean result = true;
        if (acdm.getTransactionCode().isAdm()) {
            if (isZero(acdm.getAirlineCalculations().getFare())) {
                addToContext(context, "airlineCalculations.fare", ACDMD_FARE_MISSING_MSG);
                result = false;
            }
            if (!isZero(acdm.getAgentCalculations().getFare())) {
                addToContext(context, "agentCalculations.fare", ACDMD_NO_AMOUNT_PERMITTED_MSG);
                result = false;
            }
        } else {
            if (isZero(acdm.getAgentCalculations().getFare())) {
                addToContext(context, "agentCalculations.fare", ACDMD_FARE_MISSING_MSG);
                result = false;
            }
            if (!isZero(acdm.getAirlineCalculations().getFare())) {
                addToContext(context, "airlineCalculations.fare", ACDMD_NO_AMOUNT_PERMITTED_MSG);
                result = false;
            }
        }

        if (!isValidAcdmd(acdm.getAirlineCalculations(), "airlineCalculations", context)) {
            result = false;
        }
        if (!isValidAcdmd(acdm.getAgentCalculations(), "agentCalculations", context)) {
            result = false;
        }

        return result;
    }

    private boolean isValidAcdmd(CalculationsRequest calculations, String field,
            ConstraintValidatorContext context) {
        boolean result = true;
        if (!isZero(calculations.getCommission())) {
            addToContext(context, field + ".commission", ACDMD_NO_AMOUNT_PERMITTED_MSG);
            result = false;
        }
        if (!isZero(calculations.getSpam())) {
            addToContext(context, field + ".spam", ACDMD_NO_AMOUNT_PERMITTED_MSG);
            result = false;
        }
        if (!isZero(calculations.getTax())) {
            addToContext(context, field + ".tax", ACDMD_NO_AMOUNT_PERMITTED_MSG);
            result = false;
        }
        if (!isZero(calculations.getTaxOnCommission())) {
            addToContext(context, field + ".taxOnCommission", ACDMD_NO_AMOUNT_PERMITTED_MSG);
            result = false;
        }
        return result;
    }

    private boolean isValidToca(Config cfg, AcdmRequest acdm, ConstraintValidatorContext context) {
        boolean airlineTocaReported = !isZero(acdm.getAirlineCalculations().getTaxOnCommission());
        boolean agentTocaReported = !isZero(acdm.getAgentCalculations().getTaxOnCommission());

        if (!cfg.getTaxOnCommissionEnabled()) {
            if (agentTocaReported) {
                addToContext(context, "agentCalculations.taxOnCommission", TOCA_NOT_PERMITTED_MSG);
                return false;
            }
            if (airlineTocaReported) {
                addToContext(context, "airlineCalculations.taxOnCommission",
                        TOCA_NOT_PERMITTED_MSG);
                return false;
            }
            return true;
        }

        if (acdm.getTaxOnCommissionType() != null
                && acdm.getTaxOnCommissionType().trim().length() > 0) {
            if (!airlineTocaReported && !agentTocaReported) {
                addToContext(context, "taxOnCommissionType", TCTP_NOT_PERMITTED_MSG);
                return false;
            }
            return true;
        }

        if (airlineTocaReported || agentTocaReported) {
            addToContext(context, "taxOnCommissionType", TCTP_MISSING_MSG);
            return false;
        }

        return true;
    }


    private boolean totalTaxCheck(AcdmRequest acdm, ConstraintValidatorContext context) {
        boolean result = true;
        BigDecimal agentTotalTax = acdm.getAgentCalculations().getTax();
        BigDecimal agentTaxSum = taxSum(acdm, TaxMiscellaneousFeeRequest::getAgentAmount);

        if (agentTotalTax.compareTo(agentTaxSum) != 0) {
            addToContext(context, "agentCalculations.tax", INCORRECT_TAX_SUM_MSG);
            result = false;
        }

        BigDecimal airlineTotalTax = acdm.getAirlineCalculations().getTax();
        BigDecimal airlineTaxSum = taxSum(acdm, TaxMiscellaneousFeeRequest::getAirlineAmount);
        if (airlineTotalTax.compareTo(airlineTaxSum) != 0) {
            addToContext(context, "airlineCalculations.tax", INCORRECT_TAX_SUM_MSG);
            result = false;
        }

        return result;
    }


    private BigDecimal taxSum(AcdmRequest acdm,
            Function<TaxMiscellaneousFeeRequest, BigDecimal> amountFunction) {
        return acdm.getTaxMiscellaneousFees().stream().filter(Objects::nonNull).map(amountFunction)
            .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private boolean netReportingCheck(Config cfg, AcdmRequest acdm,
            ConstraintValidatorContext context) {

        if (!cfg.getNridAndSpamEnabled()) {
            if (acdm.getNetReporting()) {
                addToContext(context, "netReporting", NO_NR_PERMITTED_MSG);
                return false;
            }

            boolean result = true;

            if (!isZero(acdm.getAirlineCalculations().getSpam())) {
                addToContext(context, "airlineCalculations.spam", NO_SPAM_PERMITTED_MSG);
                result = false;
            }

            if (!isZero(acdm.getAgentCalculations().getSpam())) {
                addToContext(context, "agentCalculations.spam", NO_SPAM_PERMITTED_MSG);
                result = false;
            }

            return result;
        }

        if (!acdm.getNetReporting()) {
            boolean result = true;

            if (!isZero(acdm.getAirlineCalculations().getSpam())) {
                addToContext(context, "airlineCalculations.spam", SPAM_ONLY_IN_NR_MSG);
                result = false;
            }

            if (!isZero(acdm.getAgentCalculations().getSpam())) {
                addToContext(context, "agentCalculations.spam", SPAM_ONLY_IN_NR_MSG);
                result = false;
            }

            return result;
        }

        return true;
    }


    private boolean isValidTotal(Config cfg, AcdmRequest acdm,
            ConstraintValidatorContext context) {
        String amountPaidByCustomer = "amountPaidByCustomer";

        BigDecimal tocaSignFactor = BigDecimal.valueOf(cfg.getTaxOnCommissionSign());

        Calculations calc = calculationsDifference(acdm);
        BigDecimal total = calc.getFare().add(calc.getTax())
                .subtract(calc.getCommission())
                .subtract(calc.getSpam())
                .add(tocaSignFactor.multiply(calc.getTaxOnCommission()));

        boolean concernsRfnd = ConcernsIndicator.R.equals(acdm.getConcernsIndicator());
        boolean isAdm = acdm.getTransactionCode().isAdm();

        if (total.signum() <= 0) {
            if (!concernsRfnd && isAdm) {
                addToContext(context, amountPaidByCustomer, INCORRECT_TOTAL_ADM_ISSUE_MSG);
                return false;
            }
            if (concernsRfnd && !isAdm) {
                addToContext(context, amountPaidByCustomer, INCORRECT_TOTAL_ACM_RFND_MSG);
                return false;
            }
        }

        if (total.signum() >= 0) {
            if (!concernsRfnd && !isAdm) {
                addToContext(context, amountPaidByCustomer, INCORRECT_TOTAL_ACM_ISSUE_MSG);
                return false;
            }
            if (concernsRfnd && isAdm) {
                addToContext(context, amountPaidByCustomer, INCORRECT_TOTAL_ADM_RFND_MSG);
                return false;
            }
        }

        if (total.abs().compareTo(acdm.getAmountPaidByCustomer()) != 0) {
            addToContext(context, amountPaidByCustomer, INCORRECT_TOTAL_MSG);
            return false;
        }

        return true;
    }


    private boolean isValidRegularized(Config cfg, AcdmRequest acdm,
            ConstraintValidatorContext context) {

        if (acdm.getRegularized() == null) {
            return true;
        }

        boolean isToRegularize = isToRegularize(cfg, acdm);

        if (isToRegularize && !acdm.getRegularized()) {
            addToContext(context, "regularized", REGULARIZED_MSG);
            return false;
        }

        if (!isToRegularize && acdm.getRegularized()) {
            addToContext(context, "regularized", NO_REGULARIZED_MSG);
            return false;
        }

        return true;
    }


    private boolean calculationsHasNullValue(CalculationsRequest calculations) {
        return calculations == null
            || calculations.getCommission() == null
            || calculations.getFare() == null
            || calculations.getSpam() == null
            || calculations.getTax() == null
            || calculations.getTaxOnCommission() == null;
    }


    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
