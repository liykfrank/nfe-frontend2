package org.iata.bsplink.agencymemo.test.fixtures;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.Reason;

public class ReasonFixtures {

    public static List<Reason> getReasons() {

        return Arrays.asList(getReason(1), getReason(2));
    }

    private static Reason getReason(Integer index) {

        Reason reason = new Reason();
        reason.setTitle("Title reason " + index);
        reason.setDetail("Detail reason " + index);
        reason.setIsoCountryCode("C" + index);

        return reason;
    }

}
