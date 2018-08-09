package org.iata.bsplink.refund.test.fixtures;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.model.entity.Reason;
import org.iata.bsplink.refund.model.entity.ReasonType;


public class ReasonFixtures {

    /**
     * Returns Reasons.
     */
    public static List<Reason> getReasons() {
        Reason reason = new Reason();
        reason.setType(ReasonType.REASON_FOR_ISSUANCE);
        reason.setTitle("Title reason 1");
        reason.setDetail("Detail reason 1");
        reason.setIsoCountryCode("AA");
        List<Reason> reasons = new ArrayList<>();
        reasons.add(reason);

        reason = new Reason();
        reason.setType(ReasonType.REASON_FOR_ISSUANCE);
        reason.setTitle("Title reason 2");
        reason.setDetail("Detail reason 2");
        reason.setIsoCountryCode("AA");
        reasons.add(reason);

        reason = new Reason();
        reason.setType(ReasonType.REASON_FOR_REJECTION);
        reason.setTitle("Title reason 3");
        reason.setDetail("Detail reason 3");
        reason.setIsoCountryCode("AA");
        reasons.add(reason);

        reason = new Reason();
        reason.setType(ReasonType.REASON_FOR_REJECTION);
        reason.setTitle("Title reason 4");
        reason.setDetail("Detail reason 4");
        reason.setIsoCountryCode("BB");
        reasons.add(reason);


        reason = new Reason();
        reason.setType(ReasonType.REASON_FOR_ISSUANCE);
        reason.setTitle("Title reason 5");
        reason.setDetail("Detail reason 5");
        reason.setIsoCountryCode("BB");
        reasons.add(reason);

        return reasons;
    }
}
