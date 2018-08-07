package org.iata.bsplink.agencymemo.utils;

import java.math.BigDecimal;
import java.util.Objects;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CalculationsRequest;
import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.entity.Calculations;
import org.iata.bsplink.agencymemo.model.entity.Config;

public class CalculationsUtility {

    private CalculationsUtility() {
    }


    /**
     * Returns a Calculations instance with the difference of airline's and agent's calculations.
     */
    public static Calculations calculationsDifference(AcdmRequest acdm) {

        CalculationsRequest air = acdm.getAirlineCalculations();
        CalculationsRequest agn = acdm.getAgentCalculations();
        Calculations calc = new Calculations();
        calc.setCommission(air.getCommission().subtract(agn.getCommission()));
        calc.setFare(air.getFare().subtract(agn.getFare()));
        calc.setSpam(air.getSpam().subtract(agn.getSpam()));
        calc.setTax(air.getTax().subtract(agn.getTax()));
        calc.setTaxOnCommission(air.getTaxOnCommission().subtract(agn.getTaxOnCommission()));
        return calc;
    }


    /**
     * Returns true if a regularization is to apply.
     */
    public static boolean isToRegularize(Config cfg, AcdmRequest acdm) {

        if (isTaxRegularized(acdm)) {

            return true;
        }

        final int acdmSignum = acdmSignum(acdm);
        Calculations calc = calculationsDifference(acdm);

        if (hasOppositeSignum(acdmSignum, calc.getFare())) {

            return true;
        }

        if (hasOppositeSignum(acdmSignum, calc.getTax())) {

            return true;
        }

        boolean tocaIsPositive = BigDecimal.valueOf(cfg.getTaxOnCommissionSign()).signum() > 0;

        if (isZero(calc.getFare()) && isZero(calc.getTax())) {

            return isToRegularizeTaxAndFareZero(acdmSignum, calc,
                    totalCommissionsAreDifferent(acdm));
        }

        return isToRegularizeTaxAndFareNotZero(tocaIsPositive, acdmSignum, calc);
    }


    public static boolean isZero(BigDecimal amount) {
        return amount.signum() == 0;
    }


    private static boolean isTaxRegularized(AcdmRequest acdm) {

        if (acdm.getTaxMiscellaneousFees() == null) {

            return false;
        }

        final int acdmSignum = acdmSignum(acdm);

        return acdm.getTaxMiscellaneousFees().stream()
            .filter(Objects::nonNull)
            .filter(t -> t.getAirlineAmount() != null && t.getAgentAmount() != null)
            .map(t -> t.getAirlineAmount().subtract(t.getAgentAmount()).signum())
            .anyMatch(s -> s != 0 && s != acdmSignum);
    }


    private static boolean totalCommissionsAreDifferent(AcdmRequest acdm) {

        CalculationsRequest air = acdm.getAirlineCalculations();
        CalculationsRequest agn = acdm.getAgentCalculations();
        BigDecimal airTotalCommission = air.getCommission().add(air.getSpam());
        BigDecimal agnTotalCommission = agn.getCommission().add(agn.getSpam());
        return airTotalCommission.compareTo(agnTotalCommission) != 0;
    }


    private static int acdmSignum(AcdmRequest acdm) {

        boolean concernsRfnd = ConcernsIndicator.R.equals(acdm.getConcernsIndicator());
        boolean isAdm = acdm.getTransactionCode() == null || acdm.getTransactionCode().isAdm();
        return (isAdm ? 1 : -1) * (concernsRfnd ? -1 : 1);
    }


    private static boolean hasSameSignum(int acdmSignum, BigDecimal amount) {

        return acdmSignum == amount.signum();
    }


    private static boolean hasOppositeSignum(int acdmSignum, BigDecimal amount) {

        return amount.signum() != 0 && acdmSignum != amount.signum();
    }


    private static boolean isToRegularizeTaxAndFareZero(int acdmSignum,
            Calculations calc, boolean totalCommissionsAreDifferent) {

        if (hasSameSignum(acdmSignum, calc.getCommission())) {

            return true;
        }

        if (hasSameSignum(acdmSignum, calc.getSpam())) {

            return true;
        }

        return hasSameSignum(acdmSignum, calc.getTaxOnCommission())
                && totalCommissionsAreDifferent;
    }


    private static boolean isToRegularizeTaxAndFareNotZero(boolean tocaIsPositive, int acdmSignum,
            Calculations calc) {

        if (hasOppositeSignum(acdmSignum, calc.getSpam())) {

            return true;
        }

        if (hasOppositeSignum(acdmSignum, calc.getTaxOnCommission())
                && isZero(calc.getCommission())) {

            return true;
        }

        if (hasOppositeSignum(acdmSignum, calc.getTaxOnCommission())
                && hasSameSignum(acdmSignum, calc.getCommission())) {

            return true;
        }

        if (!tocaIsPositive && hasSameSignum(acdmSignum, calc.getTaxOnCommission())
                && hasOppositeSignum(acdmSignum, calc.getCommission())) {

            return true;
        }

        return tocaIsPositive && hasOppositeSignum(acdmSignum, calc.getCommission())
                && hasOppositeSignum(acdmSignum, calc.getTaxOnCommission());
    }
}
