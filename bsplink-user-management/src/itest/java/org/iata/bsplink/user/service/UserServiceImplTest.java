package org.iata.bsplink.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.iata.bsplink.commons.rest.exception.ApplicationInternalServerError;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserPreferences;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkOptionRepository;
import org.iata.bsplink.user.model.repository.BsplinkTemplateRepository;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.model.repository.UserTemplateRepository;
import org.iata.bsplink.user.preferences.Languages;
import org.iata.bsplink.user.preferences.TimeZones;
import org.iata.bsplink.user.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserServiceImplTest extends BaseUserTest {

    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTemplateRepository userTemplateRepository;

    @Autowired
    private BsplinkTemplateRepository bsplinkTemplateRepository;

    @Autowired
    private BsplinkOptionRepository bsplinkOptionRepository;

    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private Errors errors;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() throws Exception {

        this.userService =
                new UserServiceImpl(userRepository, userTemplateRepository, keycloakService);
        userPending = new User();
        userCreated = new User();
        initiatePendingUser();
        initiateCreatedUser();

        configureKeycloakMocks(userPending);
    }

    private void configureKeycloakMocks(User user) throws URISyntaxException {

        UserRepresentation userRepresentation = getUserRepresentation();

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.createUser(user)).thenReturn(responseBuilder.build());
        when(keycloakService.findUser(user)).thenReturn(userRepresentation);
        when(keycloakService.changeUserStatus(user.getUsername(), true, errors))
                .thenReturn(userRepresentation);
    }

    /**
     * Get user test.
     *
     * @throws URISyntaxException Exception.
     */
    @Test
    public void testGetUser() throws URISyntaxException {

        createUser(userPending);

        User userReturned = userService.getUser(USER_ID).get();

        commonResponseAssertions(userPending, userReturned);
    }

    /**
     * Get users by type.
     *
     * @throws URISyntaxException Exception.
     */
    @Test
    public void testGetUserByType() throws URISyntaxException {

        User firstUser = userPending;
        createUser(firstUser);

        User secondUser = userPending;
        secondUser.setUsername(NEW_USERNAME);
        secondUser.setId(NEW_USER_ID);
        createUser(secondUser, NEW_USER_ID);

        List<User> listUsers = userService.findByUserType(UserType.AIRLINE);

        assertFalse(listUsers.isEmpty());
        assertEquals(2, listUsers.size());
    }

    /**
     * Get a non-existing user test.
     */
    @Test
    public void testGetUserNotFound() {

        Optional<User> user = userService.getUser(USER_ID_NOT_EXISTS);
        assertFalse(user.isPresent());
    }

    /**
     * Creates user.
     *
     * @throws URISyntaxException when no URL provided.
     */
    @Test
    public void testCreateUser() throws URISyntaxException {

        userService.createUser(userPending, errors);
        Optional<User> userSaved = userRepository.findById(USER_ID);

        assertTrue(userSaved.isPresent());
        commonResponseAssertions(userPending, userSaved.get());

    }

    @Test
    public void testCreateUserKeycloakError() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);
        expectedException.expectMessage(UserServiceImpl.CREATE_USER_IN_KEYCLOAK_ERROR_MESSAGE);

        ResponseBuilder responseBuilder = Response.serverError();
        when(keycloakService.createUser(userPending)).thenReturn(responseBuilder.build());

        userService.createUser(userPending, errors);
    }

    @Test
    public void testCreateCreatedKeycloakNull() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);
        expectedException.expectMessage(UserServiceImpl.CREATE_USER_ERROR_MESSAGE);

        when(keycloakService.findUser(userPending)).thenReturn(null);

        userService.createUser(userPending, errors);
    }


    /**
     * Create user already exists.
     *
     * @throws URISyntaxException URISyntaxException when no URL provided.
     */
    @Test
    public void testCreateUserAlreadyExists() throws URISyntaxException {

        expectedException.expect(ApplicationValidationException.class);
        expectedException.expectMessage("Validation error");

        configureKeycloakMocks(userCreated);

        createUser(userCreated);

        userService.createUser(userCreated, errors);
    }

    @Test
    public void testCreateUserChangeKeycloakStatusError() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);
        expectedException
                .expectMessage(UserServiceImpl.CHANGE_USER_STATUS_IN_KEYCLOAK_ERROR_MESSAGE);

        when(keycloakService.changeUserStatus(any(String.class), any(Boolean.class),
                any(Errors.class))).thenReturn(null);

        userService.createUser(userPending, errors);
    }

    /**
     * Update user test.
     *
     * @throws URISyntaxException Exception.
     */
    @Test
    public void testUpdateUser() throws URISyntaxException {

        User userCreated = createUser(userPending);

        userService.updateUser(userCreated, setNewData(getBaseUser()), errors);
        Optional<User> userReturned = userRepository.findById(userCreated.getId());

        assertTrue(userReturned.isPresent());
        commonResponseAssertions(userCreated, userReturned.get());
    }

    /**
     * Delete user test.
     *
     * @throws URISyntaxException Exception.
     */
    @Test
    public void testDeleteUser() throws URISyntaxException {

        User userCreated = createUser(userPending);

        userService.deleteUser(userCreated);

        Optional<User> userReturned = userRepository.findById(userCreated.getId());

        assertFalse(userReturned.isPresent());
    }

    @Test
    public void testDeleteUserFoundInKeycloak() throws URISyntaxException {

        createUser(userPending);

        userService.deleteUser(userPending);
        Optional<User> userReturned = userRepository.findById(userPending.getId());

        assertFalse(userReturned.isPresent());
    }

    /**
     * Update user with templates.
     */
    @Test
    public void testUpdateUserWithTemplates() throws URISyntaxException {

        userTemplateRepository.deleteAll();
        bsplinkTemplateRepository.deleteAll();

        BsplinkTemplate bsplinkTemplate = new BsplinkTemplate();

        bsplinkTemplate.setId("TEMPLATE");
        bsplinkTemplate.setUserTypes(Arrays.asList(UserType.BSP));
        bsplinkTemplate.setOptions(Arrays.asList(
                bsplinkOptionRepository.findById("FileDownload").get()));
        bsplinkTemplateRepository.save(bsplinkTemplate);

        UserTemplate userTemplate = new UserTemplate();
        userTemplate.setId("USERTEMPLATE");
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("HU"));

        userPending.setUserType(UserType.BSP);

        User userCreated = createUser(userPending);

        userPending.setTemplates(new ArrayList<>());
        userPending.getTemplates().add(userTemplate);

        User userReturned = userService.updateUser(userCreated, userPending, errors);


        assertNotNull(userReturned);
        commonResponseAssertions(userPending, userReturned);
    }

    private User createUser(User user) throws URISyntaxException {

        return createUser(user, USER_ID);
    }

    private User createUser(User user, String userId) throws URISyntaxException {

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.findUser(user)).thenReturn(getUserRepresentation(userId));
        when(keycloakService.changeUserStatus(user.getUsername(), true, errors))
                .thenReturn(getUserRepresentation(userId));
        when(keycloakService.createUser(user)).thenReturn(responseBuilder.build());

        return userService.createUser(user, errors);
    }

    private User setNewData(User newUser) {
        newUser.setEmail("new-email@email.com");
        newUser.setOrganization("new-organization");
        newUser.setUserType(UserType.THIRDPARTY);
        return newUser;
    }

    private UserRepresentation getUserRepresentation() {

        return getUserRepresentation(USER_ID);
    }

    private UserRepresentation getUserRepresentation(String userId) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        return userRepresentation;
    }

    @Test
    public void testAddsDefaultPreferencesWhenUserIsCreated() throws URISyntaxException {

        userPending = userService.createUser(userPending, errors);

        assertThat(userPending.getPreferences(), notNullValue());
        assertThat(userPending.getPreferences().getLanguage(),
                equalTo(Languages.DEFAULT.toString()));
        assertThat(userPending.getPreferences().getTimeZone(), equalTo(TimeZones.DEFAULT));
    }

    @Test
    public void testUpdatesUserPreferencesFromPreferencesInstance() throws URISyntaxException {

        User userCreated = createUser(userPending);

        UserPreferences returnedUserPreferences =
                userService.updateUserPreferences(userCreated, getUpdatedPreferences());

        assertThat(returnedUserPreferences.getLanguage(), equalTo(Languages.DE.toString()));
        assertThat(returnedUserPreferences.getTimeZone(), equalTo("Europe/Berlin"));
    }

    private UserPreferences getUpdatedPreferences() {

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setLanguage(Languages.DE.toString());
        userPreferences.setTimeZone("Europe/Berlin");

        return userPreferences;
    }

    @Test
    public void testUpdatesUserPreferencesFromUserInstance() throws URISyntaxException {

        User userCreated = createUser(userPending);

        User updatedUser = getBaseUser();
        updatedUser.setPreferences(getUpdatedPreferences());

        User savedUser = userService.updateUser(userCreated, updatedUser, errors);

        assertThat(savedUser.getPreferences().getLanguage(),
                equalTo(updatedUser.getPreferences().getLanguage()));
        assertThat(savedUser.getPreferences().getTimeZone(),
                equalTo(updatedUser.getPreferences().getTimeZone()));
    }

}
