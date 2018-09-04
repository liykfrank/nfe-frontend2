package org.iata.bsplink.user.service;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.iata.bsplink.commons.rest.exception.ApplicationInternalServerError;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.model.repository.UserTemplateRepository;
import org.iata.bsplink.user.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
public class UserServiceImplTest extends BaseUserTest {

    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTemplateRepository userTemplateRepository;

    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private Errors errors;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        this.userService = new UserServiceImpl(userRepository, userTemplateRepository,
                keycloakService);
        userPending = new User();
        userCreated = new User();
        createPendingUser();
        createCreatedUser();

    }

    /**
     * Get user test.
     */
    @Test
    public void testGetUser() {

        doReturn(Optional.of(userPending)).when(userRepository).findById(USER_ID);

        userPending = userService.getUser(USER_ID).get();

        commonResponseAssertions(userPending);

        verify(userRepository, times(1)).findById(USER_ID);
    }

    /**
     * Get a non-existing user test.
     */
    @Test
    public void testGetUserNotFound() {

        Optional<User> user = userService.getUser(USER_ID);
        assertFalse(user.isPresent());
    }

    /**
     * Creates user.
     *
     * @throws URISyntaxException when no URL provided.
     */
    @Test
    public void testCreateUser() throws URISyntaxException {

        doReturn(Optional.of(userPending)).when(userRepository)
                .findByUsername(userPending.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(userPending);
        when(keycloakService.findUser(userPending)).thenReturn(getUserRepresentation());

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.createUser(userPending)).thenReturn(responseBuilder.build());

        userPending = userService.createUser(userPending, errors);

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testCreateUserAlreadyExistsInKeycloak() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);

        doReturn(Optional.empty()).when(userRepository).findByUsername(userPending.getUsername());
        when(keycloakService.findUser(userPending)).thenReturn(getUserRepresentation());

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.createUser(userPending)).thenReturn(responseBuilder.build());

        userPending = userService.createUser(userPending, errors);

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testCreateUserKeycloakError() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);

        doReturn(Optional.of(userPending)).when(userRepository)
                .findByUsername(userPending.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(userPending);
        when(keycloakService.findUser(userPending)).thenReturn(getUserRepresentation());

        ResponseBuilder responseBuilder = Response.serverError();
        when(keycloakService.createUser(userPending)).thenReturn(responseBuilder.build());

        userPending = userService.createUser(userPending, errors);

    }

    @Test
    public void testCreateCreatedKeycloakNull() throws URISyntaxException {

        doReturn(Optional.of(userCreated)).when(userRepository)
                .findByUsername(userCreated.getUsername());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(USER_ID);

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.createUser(any())).thenReturn(responseBuilder.build());
        when(keycloakService.findUser(userPending)).thenReturn(null);

        expectedException.expect(ApplicationInternalServerError.class);
        userService.createUser(userPending, errors);

    }


    /**
     * Create user already exists.
     *
     * @throws URISyntaxException URISyntaxException when no URL provided.
     */
    @Test
    public void testCreateUserAlreadyExistsWithUsername() {

        expectedException.expect(ApplicationValidationException.class);

        doReturn(Optional.of(userCreated)).when(userRepository)
                .findByUsername(userCreated.getUsername());

        when(keycloakService.findUser(userCreated)).thenReturn(getUserRepresentation());

        userCreated = userService.createUser(userCreated, errors);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUserNull() throws URISyntaxException {

        expectedException.expect(ApplicationInternalServerError.class);

        when(userRepository.save(any(User.class))).thenReturn(null);
        doReturn(Optional.of(userPending)).when(userRepository)
                .findByUsername(userPending.getUsername());
        when(keycloakService.findUser(userPending)).thenReturn(getUserRepresentation());

        ResponseBuilder responseBuilder = Response.created(new URI("mock-url"));
        when(keycloakService.createUser(userPending)).thenReturn(responseBuilder.build());

        userPending = userService.createUser(userPending, errors);

    }

    /**
     * Update user test.
     */
    @Test
    public void testUpdateUser() {

        doReturn(Optional.of(userPending)).when(userRepository).findById(USER_ID);
        doReturn(userPending).when(userRepository).save(any(User.class));

        userPending = userService.updateUser(userPending, userPending, errors);

        commonResponseAssertions(userPending);

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Delete user test.
     */
    @Test
    public void testDeleteUser() {

        doReturn(Optional.of(userPending)).when(userRepository).findById(USER_ID);
        doNothing().when(userRepository).delete(userPending);

        userService.deleteUser(userPending);

        verify(userRepository, times(1)).delete(userPending);
    }

    /**
     * Delete user not found test.
     */
    @Test
    public void testDeleteUserNotFound() {

        doReturn(Optional.empty()).when(userRepository).findById(USER_ID);

        userService.deleteUser(userPending);

        verify(userRepository, times(1)).delete(userPending);
    }

    @Test
    public void testDeleteUserFoundInKeycloak() {

        doReturn(Optional.empty()).when(userRepository).findById(USER_ID);

        when(keycloakService.findUser(any())).thenReturn(new UserRepresentation());

        userService.deleteUser(userPending);

        verify(keycloakService, times(1)).deleteUser(userPending.getId());
    }

    private UserRepresentation getUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1d26b494-64e1-41d7-8144-cd8f0b634d07");
        return userRepresentation;
    }

    /**
     * Update user test.
     */
    @Test
    public void testUpdateUserWithTemplates() {

        userPending.setTemplates(Arrays.asList(new UserTemplate()));
        doReturn(Optional.of(userPending)).when(userRepository).findById(USER_ID);
        doReturn(userPending).when(userRepository).save(any(User.class));

        userPending = userService.updateUser(userPending, userPending, errors);

        commonResponseAssertions(userPending);

        verify(userRepository, times(1)).save(any(User.class));
    }

}