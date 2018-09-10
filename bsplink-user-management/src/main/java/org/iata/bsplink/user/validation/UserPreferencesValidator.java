package org.iata.bsplink.user.validation;

import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_DUPLICATED_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_EMPTY_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_FORMAT_MESSAGE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserPreferences;
import org.iata.bsplink.user.model.entity.UserRegion;
import org.iata.bsplink.user.preferences.Languages;
import org.iata.bsplink.user.preferences.TimeZones;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class UserPreferencesValidator implements Validator {

    private static final String LANGUAGE_EMPTY_MESSAGE = "Language preference can not be empty";
    private static final String NOT_SUPPORTED_LANGUAGE_MESSAGE_PATTERN =
            "Not supported language preference '%s'";
    private static final String PREFERENCES_FIELD = "preferences";
    private static final String LANGUAGE_FIELD = "language";
    private static final String TIME_ZONE_EMPTY_MESSAGE = "Time zone can not be empty";
    private static final String TIME_ZONE_FIELD = "timeZone";
    private static final String INVALID_TIME_ZONE_MESSAGE_PATTERN = "Invalid time zone '%s'";
    private static final String REGION_NAME_EMPTY_MESSAGE = "Region name can not be empty";
    private static final String REGION_NAME_UNIQUE = "Region name must be unique";
    private static final String ONLY_ONE_DEFAULT_REGION_MESSAGE =
            "Only one region can be defined as default";
    private static final String DEFAULT_REGION_REQUIRED_MESSAGE = "There must be a default region";

    private boolean isUserObject = false;
    private Errors errors;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz)
                || UserPreferences.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        this.errors = errors;

        UserPreferences userPreferences;

        if (target instanceof User) {

            userPreferences = ((User) target).getPreferences();
            isUserObject = true;

        } else {

            userPreferences = ((UserPreferences) target);
            isUserObject = false;
        }

        if (userPreferences == null) {

            return;
        }

        validateLanguage(userPreferences, errors);
        validateTimeZone(userPreferences, errors);
        validateRegions(userPreferences, errors);
    }

    private void validateLanguage(UserPreferences userPreferences, Errors errors) {

        String language = userPreferences.getLanguage();

        if (StringUtils.isEmpty(language)) {

            addError(LANGUAGE_FIELD, LANGUAGE_EMPTY_MESSAGE);
            return;
        }

        if (!Languages.exists(language)) {

            addError(LANGUAGE_FIELD,
                    String.format(NOT_SUPPORTED_LANGUAGE_MESSAGE_PATTERN, language));
        }
    }

    private void addError(String fieldName, String message) {

        //If the validated object is of type User it is necessary to add the field of object that
        //contains the preferences.

        String realFieldName = isUserObject ? PREFERENCES_FIELD + "." + fieldName : fieldName;

        errors.rejectValue(realFieldName, "", message);
    }

    private void validateTimeZone(UserPreferences userPreferences, Errors errors) {

        String timeZone = userPreferences.getTimeZone();

        if (StringUtils.isEmpty(timeZone)) {

            addError(TIME_ZONE_FIELD, TIME_ZONE_EMPTY_MESSAGE);
            return;
        }

        if (!TimeZones.exists(timeZone)) {

            addError(TIME_ZONE_FIELD, String.format(INVALID_TIME_ZONE_MESSAGE_PATTERN, timeZone));
        }
    }

    // TODO: this needs refactoring
    // UserTemplateValidator.validateIsoCountryCodes has almost the same functionality
    private void validateRegions(UserPreferences userPreferences, Errors errors) {

        List<String> regionNames = new ArrayList<>();

        int index = 0;
        boolean thereIsDefaultRegion = false;

        for (UserRegion region : userPreferences.getRegions()) {

            if (StringUtils.isEmpty(region.getName())) {

                addError(getRegionsFieldName(index), REGION_NAME_EMPTY_MESSAGE);
            }

            if (regionNames.contains(region.getName())) {

                addError(getRegionsFieldName(index), REGION_NAME_UNIQUE);
            }

            if (region.isDefault() && thereIsDefaultRegion) {

                addError(getRegionsFieldName(index), ONLY_ONE_DEFAULT_REGION_MESSAGE);
            }

            thereIsDefaultRegion = thereIsDefaultRegion || region.isDefault();

            validateCountries(region, errors, index);

            regionNames.add(region.getName());
            index++;
        }

        if (!(userPreferences.getRegions().isEmpty() || thereIsDefaultRegion)) {

            addError("regions", DEFAULT_REGION_REQUIRED_MESSAGE);
        }
    }

    private String getRegionsFieldName(int regionIndex) {

        return String.format("regions[%d]", regionIndex);
    }

    private void validateCountries(UserRegion region, Errors errors, int regionsIndex) {

        if (region.getIsoCountryCodes().isEmpty()) {

            addError(getRegionsFieldName(regionsIndex), "Region must have at least one country");
        }

        int countryIndex = 0;
        List<String> countryNames = new ArrayList<>();

        for (String country : region.getIsoCountryCodes()) {

            if (StringUtils.isEmpty(country)) {

                addError(getCountryFieldName(regionsIndex, countryIndex), ISOC_EMPTY_MESSAGE);

            } else {

                if (!country.matches("^[A-Z][0-9A-Z]$")) {

                    addError(getCountryFieldName(regionsIndex, countryIndex), ISOC_FORMAT_MESSAGE);
                    continue;
                }

                if (countryNames.contains(country)) {

                    addError(getCountryFieldName(regionsIndex, countryIndex),
                            ISOC_DUPLICATED_MESSAGE);
                }
            }

            countryNames.add(country);
            countryIndex++;
        }
    }

    private String getCountryFieldName(int regionsIndex, int countryIndex) {

        return String.format("regions[%d].isoCountryCodes[%d]", regionsIndex,
                countryIndex);
    }
}
