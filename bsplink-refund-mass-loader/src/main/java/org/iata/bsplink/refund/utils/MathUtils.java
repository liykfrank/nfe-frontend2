package org.iata.bsplink.refund.utils;

import java.math.BigDecimal;

public class MathUtils {

    private MathUtils() {
    }

    /**
     * Parses the string argument as Integer.
     * @return null, if the string does not contain a parsable integer,
     *      and in the other case the parsed Integer
     */
    public static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    /**
     * Converting string argurment ro BigDecimal and applying decimals.
     * @return BigDecimal from amount, applying the decimals
     *     (the last numDecimals digits are interpreted as decimals).
     */
    public static BigDecimal applyDecimals(String amount, Integer numDecimals) {
        if (amount == null || numDecimals == null) {
            return null;
        }
        try {
            return BigDecimal.valueOf(Long.parseLong(amount), numDecimals);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
