package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RefundStatusIssuePermissionValidator implements Validator {

    public static final String NO_PERMISSION = "No permission to issue refunds for the airline.";

    private RefundIssuePermissionService permissionService;

    public RefundStatusIssuePermissionValidator(RefundIssuePermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Boolean permission = permissionService.isPermitted((Refund) target);

        if (permission != null && !permission) {
            errors.rejectValue("airlineCode", "field.not_found", NO_PERMISSION);
        }
    }
}
