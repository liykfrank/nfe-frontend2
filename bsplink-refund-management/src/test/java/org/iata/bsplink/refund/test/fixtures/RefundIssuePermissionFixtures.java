package org.iata.bsplink.refund.test.fixtures;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.model.entity.RefundIssuePermission;

public class RefundIssuePermissionFixtures {

    /**
     * Returns list of Refund Issue Permissions.
     */
    public static List<RefundIssuePermission> getRefundIssuePermissions() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode("AA");
        permission.setAirlineCode("220");
        permission.setAgentCode("78200011");

        List<RefundIssuePermission> permissions = new ArrayList<>();
        permissions.add(permission);

        permission = new RefundIssuePermission();
        permission.setAirlineCode("220");
        permission.setIsoCountryCode("AA");
        permission.setAgentCode("78200012");

        return permissions;
    }
}
