package org.iata.bsplink.user.validation;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.pojo.Agent;
import org.iata.bsplink.user.service.AgentService;
import org.iata.bsplink.user.service.AirlineService;
import org.iata.bsplink.user.service.BsplinkTemplateService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserTemplateValidator implements Validator {

    public static final String TEMPLATE_NOT_AVAILABLE_FOR_USERTYPE_MESSAGE =
            "The template is not available for the user type.";
    public static final String TEMPLATE_DUPLICATED_MESSAGE =
            "Templates for a user have to be unique.";
    public static final String ISOC_DUPLICATED_MESSAGE =
            "ISO Country Code to be assigned only once.";
    public static final String TEMPLATE_NOT_FOUND_MESSAGE =
            "The template was not found.";
    public static final String ISOC_NULL_MESSAGE =
            "The ISO Country Code cannot be null.";
    public static final String TEMPLATE_NULL_MESSAGE =
            "The Template cannot be null.";
    public static final String ISOC_FORMAT_MESSAGE =
            "ISO Country Code format is incorrect.";
    public static final String ISOC_AIRLINE_MESSAGE =
            "The airline does not exist in the country corresponding to the ISO Country Code.";
    public static final String AGENT_NOT_FOUND_MESSAGE =
            "The agent was not found.";
    public static final String ISOC_AGENT_MESSAGE =
            "The agent does not exist in the country corresponding to the ISO Country Code.";


    private BsplinkTemplateService bsplinkTemplateService;
    private AirlineService airlineService;
    private AgentService agentService;

    /**
     * Creates an instance of the validator for the UserTemplate of a User.
     */
    public UserTemplateValidator(BsplinkTemplateService bsplinkTemplateService,
            AirlineService airlineService, AgentService agentService) {
        this.bsplinkTemplateService = bsplinkTemplateService;
        this.airlineService = airlineService;
        this.agentService = agentService;
    }



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

        validateNullTemplate(user, errors);
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




    private void validateNullTemplate(User user, Errors errors) {

        int t = -1;

        for (UserTemplate template: user.getTemplates()) {

            t++;

            if (template == null) {

                reject(errors, t, TEMPLATE_NULL_MESSAGE);
            }
        }
    }


    private void validateIsoCountryCodes(User user, Errors errors) {
        int t = -1;

        for (UserTemplate template: user.getTemplates()) {

            t++;

            if (template == null || template.getIsoCountryCodes() == null) {

                continue;
            }

            int i = -1;

            for (String isoc : template.getIsoCountryCodes()) {

                i++;

                if (isoc == null) {

                    reject(errors, t, i, ISOC_NULL_MESSAGE);

                } else if (!isoc.matches("^[A-Z][0-9A-Z]$")) {

                    reject(errors, t, i, ISOC_FORMAT_MESSAGE);

                } else if (i > 0 && template.getIsoCountryCodes().subList(0, i).contains(isoc)) {

                    reject(errors, t, i, ISOC_DUPLICATED_MESSAGE);

                } else {

                    validateUserCountry(t, i, isoc, user, errors);
                }
            }
        }
    }



    private void validateUserCountry(int templateNr, int isocNr, String isoc, User user,
            Errors errors) {

        if (UserType.AIRLINE.equals(user.getUserType())
                && user.getUserCode() != null
                && airlineService.findAirline(isoc, user.getUserCode()) == null) {

            reject(errors, templateNr, isocNr, ISOC_AIRLINE_MESSAGE);

        } else if (UserType.AGENT.equals(user.getUserType())
                && user.getUserCode() != null) {

            Agent agent = agentService.findAgent(user.getUserCode());

            if (agent == null) {

                reject(errors, templateNr, isocNr, AGENT_NOT_FOUND_MESSAGE);
            } else if (!isoc.equals(agent.getIsoCountryCode())) {

                reject(errors, templateNr, isocNr, ISOC_AGENT_MESSAGE);
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
                    bsplinkTemplateService.findById(template.getTemplate());

            if (template.getTemplate() == null || !templateOpt.isPresent()) {

                reject(errors, i, TEMPLATE_NOT_FOUND_MESSAGE);
            } else {

                List<UserType> templateUserTypes = templateOpt.get().getUserTypes();

                if (templateUserTypes != null && !templateUserTypes.contains(user.getUserType())) {

                    reject(errors, i, TEMPLATE_NOT_AVAILABLE_FOR_USERTYPE_MESSAGE);
                }
            }
        }
    }


    private void reject(Errors errors, int templateNr, int isocNr, String message) {

        errors.rejectValue("templates[" + templateNr + "].isoCountryCodes[" + isocNr + "]", "",
                message);
    }


    private void reject(Errors errors, int templateNr, String message) {

        errors.rejectValue("templates[" + templateNr + "]", "", message);
    }
}
