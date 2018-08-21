package org.iata.bsplink.user.validation;

import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.service.BsplinkOptionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BsplinkTemplateOptionUserTypeValidator {

    public static final String INVALID_OPTION_FOR_USER_TYPE_MSG =
            "The option is not an option for all user types of the template.";
    public static final String OPTION_NOT_FOUND_MSG = "The option was not found.";
    public static final String INVALID_USER_TYPE_MSG =
            "The template has an option which is not available for the user type.";

    private BsplinkOptionService optionService;

    public BsplinkTemplateOptionUserTypeValidator(BsplinkOptionService optionService) {
        this.optionService = optionService;
    }


    /**
     * Validates user type of a template's option.
     */
    public void validate(BsplinkTemplate template, BsplinkOption option, Errors errors) {

        if (!isValidOptionForTemplate(option, template)) {

            errors.rejectValue("userTypes", "", INVALID_OPTION_FOR_USER_TYPE_MSG);
        }
    }


    /**
     * Validates user type of all template's options.
     */
    public void validate(BsplinkTemplate template, Errors errors) {

        if (template.getOptions() == null) {

            return;
        }

        int i = -1;

        for (BsplinkOption option : template.getOptions()) {

            i++;

            Optional<BsplinkOption> optionOpt = optionService.findById(option.getId());

            if (!optionOpt.isPresent()) {

                errors.rejectValue("options[" + i + "]", "", OPTION_NOT_FOUND_MSG);
            } else if (!isValidOptionForTemplate(optionOpt.get(), template)) {

                errors.rejectValue("options[" + i + "]", "", INVALID_OPTION_FOR_USER_TYPE_MSG);
            }
        }
    }


    /**
     * Validates user type for all options of the template.
     */
    public void validate(BsplinkTemplate template, UserType userType, Errors errors) {

        if (template.getOptions() == null || template.getOptions().isEmpty()) {

            return;
        }

        for (BsplinkOption option : template.getOptions()) {

            if (!option.getUserTypes().contains(userType)) {

                errors.rejectValue("", "", INVALID_USER_TYPE_MSG);
            }
        }
    }


    private boolean isValidOptionForTemplate(BsplinkOption option,
            BsplinkTemplate template) {

        if (template.getUserTypes() == null || option == null || option.getUserTypes() == null
                || option.getUserTypes().isEmpty()) {

            return false;
        }

        for (UserType userType: template.getUserTypes()) {

            if (!option.getUserTypes().contains(userType)) {

                return false;
            }
        }

        return true;
    }
}
