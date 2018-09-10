package org.iata.bsplink.user.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_DUPLICATED_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_EMPTY_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_FORMAT_MESSAGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserPreferences;
import org.iata.bsplink.user.model.entity.UserRegion;
import org.iata.bsplink.user.preferences.Languages;
import org.iata.bsplink.user.preferences.TimeZones;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@RunWith(JUnitParamsRunner.class)
public class UserPreferencesValidatorTest {

    private Errors errors;
    private User user;
    private UserPreferences userPreferences;
    private UserPreferencesValidator validator;

    @Before
    public void setUp() {

        user = new User();

        userPreferences = new UserPreferences();
        userPreferences.setLanguage(Languages.DEFAULT.toString());
        userPreferences.setTimeZone(TimeZones.DEFAULT);

        user.setPreferences(userPreferences);

        errors = new BeanPropertyBindingResult(user, "user");
        validator = new UserPreferencesValidator();
    }

    @Test
    public void testDoesNothingIfPreferencesIsNull() {

        user.setPreferences(null);

        doValidation();

        assertFalse(errors.hasErrors());
    }

    private void doValidation() {

        validator.validate(user, errors);
    }

    @Test
    @Parameters
    public void testValidatesLanguage(String language, boolean hasErrors, String errorMessage) {

        userPreferences.setLanguage(language);

        doValidation();

        assertErrors(hasErrors, errorMessage, "preferences.language");
    }

    private void assertErrors(boolean hasErrors, String errorMessage, String fieldName) {

        assertThat(errors.hasErrors(), equalTo(hasErrors));

        if (hasErrors) {

            assertThat(errors.getFieldErrors(), hasSize(1));
        }

        if (!errorMessage.isEmpty()) {

            FieldError fieldError = errors.getFieldErrors().get(0);

            assertThat(fieldError.getDefaultMessage(), equalTo(errorMessage));
            assertThat(fieldError.getField(), equalTo(fieldName));
        }
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesLanguage() {

        List<Object> parameters = new ArrayList<>();

        for (Languages language : Languages.values()) {

            parameters.add(new Object[] { language.toString(), false, "" });
        }

        parameters.add(new Object[] { "foo", true, "Not supported language preference 'foo'" });
        parameters.add(new Object[] { "", true, "Language preference can not be empty" });
        parameters.add(new Object[] { null, true, "Language preference can not be empty" });

        return parameters.toArray(new Object[][] {});
    }

    @Test
    @Parameters
    public void testValidatesTimeZone(String timeZone, boolean hasErrors, String errorMessage) {

        userPreferences.setTimeZone(timeZone);

        doValidation();

        assertErrors(hasErrors, errorMessage, "preferences.timeZone");
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesTimeZone() {

        return new Object[][] {

            { "foo", true, "Invalid time zone 'foo'" },
            { "", true, "Time zone can not be empty" },
            { "Europe/Madrid", false, "" },
            { "UTC", false, "" },
            { "Pacific/Wake", false, "" },
        };
    }

    @Test
    @Parameters
    public void testValidatesRegions(List<UserRegion> regions, boolean hasErrors,
            String errorMessage, String errorField) {

        userPreferences.setRegions(regions);

        doValidation();

        assertErrors(hasErrors, errorMessage, errorField);
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesRegions() {

        UserRegion regionOk = createRegion("Region1", true);

        return new Object[][] {

            {
                Arrays.asList(regionOk, createRegion("")), true,
                "Region name can not be empty", "preferences.regions[1]"
            },
            {
                Arrays.asList(regionOk, createRegion(null, false)),
                true, "Region name can not be empty", "preferences.regions[1]"
            },
            {
                Arrays.asList(regionOk, createRegion("Region1", false)),
                true, "Region name must be unique", "preferences.regions[1]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", new String[] {}), regionOk),
                true, "Region must have at least one country", "preferences.regions[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", "E"), regionOk),
                true, ISOC_FORMAT_MESSAGE, "preferences.regions[0].isoCountryCodes[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", "00"), regionOk),
                true, ISOC_FORMAT_MESSAGE, "preferences.regions[0].isoCountryCodes[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", "EEE"), regionOk),
                true, ISOC_FORMAT_MESSAGE, "preferences.regions[0].isoCountryCodes[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", ""), regionOk),
                true, ISOC_EMPTY_MESSAGE, "preferences.regions[0].isoCountryCodes[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", (String)null), regionOk),
                true, ISOC_EMPTY_MESSAGE, "preferences.regions[0].isoCountryCodes[0]"
            },
            {
                Arrays.asList(createRegion("WrongRegion", "ES", "ES"), regionOk), true,
                ISOC_DUPLICATED_MESSAGE,
                "preferences.regions[0].isoCountryCodes[1]"
            },
            {
                Arrays.asList(regionOk, createRegion("Region2", true)), true,
                "Only one region can be defined as default",
                "preferences.regions[1]"
            },
            {
                Arrays.asList(createRegion("Region1", "ES"), createRegion("Region2", "GB")), true,
                "There must be a default region",
                "preferences.regions"
            }
        };
    }

    private UserRegion createRegion(String regionName) {

        return createRegion(regionName, "AA", "BB", "CC");
    }

    private UserRegion createRegion(String regionName, boolean isDefault) {

        UserRegion region = createRegion(regionName, "AA", "BB", "CC");
        region.setDefault(isDefault);

        return region;
    }

    private UserRegion createRegion(String regionName, String... isoCountryCodes) {

        UserRegion region = new UserRegion();

        region.setName(regionName);

        for (String isoCountryCode : isoCountryCodes) {

            region.getIsoCountryCodes().add(isoCountryCode);
        }

        return region;
    }

    @Test
    public void testSupports() {

        assertTrue(validator.supports(User.class));
        assertTrue(validator.supports(UserPreferences.class));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testCanValidateUserPreferences() {

        userPreferences.setLanguage("foo");

        errors = new BeanPropertyBindingResult(userPreferences, "userPreferences");
        validator.validate(userPreferences, errors);

        assertErrors(true, "Not supported language preference 'foo'", "language");
    }

}
