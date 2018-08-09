package org.iata.bsplink.refund.validation;

import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class IssuePermissionValidator implements Validator {

    public static final String NO_PERMISSION = "No permission to issue refunds for the airline.";

    private RefundIssuePermissionService permissionService;

    public IssuePermissionValidator(RefundIssuePermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;

        if (refund.getAirlineCode() == null
                || refund.getIsoCountryCode() == null
                || refund.getAgentCode() == null) {
            return;
        }

        Optional<RefundIssuePermission> permission =
                permissionService.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                        refund.getIsoCountryCode(), refund.getAirlineCode(), refund.getAgentCode());

        if (!permission.isPresent()) {
            errors.rejectValue("airlineCode", "field.not_found", NO_PERMISSION);
        }
    }
}
