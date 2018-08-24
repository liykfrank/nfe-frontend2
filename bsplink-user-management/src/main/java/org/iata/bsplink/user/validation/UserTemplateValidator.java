package org.iata.bsplink.user.validation;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.service.BsplinkTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserTemplateValidator implements Validator {

    public static final String TEMPLATE_NOT_AVAILABLE_FOR_USERTYPE_MESSAGE =
            "The template is not available for the user type.";
    public static final String TEMPLATE_DUPLICATED_MESSAGE =
            "Templates for a user have to be unique.";
    public static final String TEMPLATE_NOT_FOUND_MESSAGE =
            "The template was not found.";
    public static final String ISOC_NULL_MESSAGE =
            "The ISO Country Code cannot be null.";
    public static final String ISOC_DUPLICATED_MESSAGE =
            "ISO Country Codes in a template have to be unique.";
    public static final String ISOC_FORMAT_MESSAGE =
            "ISO Country Code format is incorrect.";


    @Autowired
    private BsplinkTemplateService bsplinkTemplateService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }



    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        if (user.getTemplates() == null || user.getTemplates().isEmpty()) {

            return;
        }

        validateUserTypes(user, errors);

        validateTemplateUniqueness(user, errors);

        validateIsoCountryCodes(user, errors);
    }



    private void validateTemplateUniqueness(User user, Errors errors) {
        int i = -1;

        for (UserTemplate template: user.getTemplates()) {

            i++;

            if (i > 0 && template.getTemplate() != null
                    && user.getTemplates().stream().limit(i).map(UserTemplate::getTemplate)
                        .anyMatch(template.getTemplate()::equals)) {

                reject(errors, i, TEMPLATE_DUPLICATED_MESSAGE);
            }
        }
    }



    private void validateIsoCountryCodes(User user, Errors errors) {
        int t = -1;

        for (UserTemplate template: user.getTemplates()) {

            t++;

            if (template.getIsoCountryCodes() != null) {
                int i = -1;
                for (String isoc: template.getIsoCountryCodes()) {

                    i++;

                    if (isoc == null) {

                        rejectIsoc(errors, t, i, ISOC_NULL_MESSAGE);

                    } else if (!isoc.matches("^[A-Z][0-9A-Z]$")) {

                        rejectIsoc(errors, t, i, ISOC_FORMAT_MESSAGE);

                    } else if (template.getIsoCountryCodes().stream().limit(i)
                        .anyMatch(isoc::equals)) {

                        rejectIsoc(errors, t, i, ISOC_DUPLICATED_MESSAGE);

                    }
                }
            }
        }
    }


    private void validateUserTypes(User user, Errors errors) {

        if (user.getUserType() == null) {
            return;
        }

        int i = -1;

        for (UserTemplate template: user.getTemplates()) {

            i++;

            if (template.getTemplate() == null) {

                continue;
            }

            Optional<BsplinkTemplate> templateOpt =
                    bsplinkTemplateService.findById(template.getTemplate().getId());

            if (template.getTemplate().getId() == null || !templateOpt.isPresent()) {

                reject(errors, i, TEMPLATE_NOT_FOUND_MESSAGE);
            } else {

                List<UserType> templateUserTypes = templateOpt.get().getUserTypes();

                if (templateUserTypes != null && !templateUserTypes.contains(user.getUserType())) {

                    reject(errors, i, TEMPLATE_NOT_AVAILABLE_FOR_USERTYPE_MESSAGE);
                }
            }
        }
    }

    private void rejectIsoc(Errors errors, int templateNr, int isocNr, String message) {
        errors.rejectValue("templates[" + templateNr + "].isoCountryCodes[" + isocNr + "]",
                "", message);
    }

    private void reject(Errors errors, int templateNr, String message) {
        errors.rejectValue("templates[" + templateNr + "]", "", message);
    }
}
