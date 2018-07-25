package org.iata.bsplink.agencymemo.fake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class Period {

    private String isoc;
    private List<Integer> values;

    /**
     * Set period by ISOC.
     * @param isoc ISO country.
     */
    public void setPeriod(String isoc) {

        if (isoc.equals("AA")) {

            this.setIsoc("AA");
            this.setValues(Arrays.asList(2018061, 2018062, 2018071, 2018072, 2018081, 2018082,
                    2018091, 2018092, 2018101, 2018102, 2018111, 2018112, 2018121, 2018122));

        } else if (isoc.equals("BB")) {

            this.setIsoc("BB");
            this.setValues(Arrays.asList(2018061, 2018062, 2018063, 2018071, 2018072, 2018072,
                    2018081, 2018082, 2018081, 2018083, 2018091, 2018092, 2018093, 2018101, 2018102,
                    2018103, 2018111, 2018112, 2018121, 2018122));

        } else if (isoc.equals("CC")) {

            this.setIsoc("CC");
            this.setValues(Arrays.asList(2018061, 2018062, 2018063, 2018071, 2018072, 2018072,
                    2018081, 2018082, 2018081, 2018083, 2018091, 2018092, 2018093, 2018101, 2018102,
                    2018103, 2018111, 2018112, 2018121, 2018122));

        }

    }

    /**
     * Returns all periods.
     * @return listPeriods A list with all periods.
     */
    public static List<Period> getPeriodsList() {

        List<Period> listPeriods = new ArrayList<>();

        Period aaPeriod = new Period();
        aaPeriod.setPeriod("AA");
        listPeriods.add(aaPeriod);

        Period bbPeriod = new Period();
        bbPeriod.setPeriod("BB");
        listPeriods.add(bbPeriod);

        Period ccPeriod = new Period();
        ccPeriod.setPeriod("CC");
        listPeriods.add(ccPeriod);
        
        return listPeriods;
    }
}
