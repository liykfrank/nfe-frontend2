package org.iata.bsplink.user.service;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.service.KeycloakService;
import org.iata.bsplink.user.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
public class KeycloakServiceTest extends BaseUserTest {

    private KeycloakService keycloakService;

    @MockBean
    private UsersResource usersResource;

    @MockBean
    private UserResource userResource;

    @MockBean
    private RealmResource realmResource;

    @MockBean
    private Keycloak keycloak;

    @MockBean
    private Errors errors;

    @MockBean
    private Response response;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        keycloakService = new KeycloakService(keycloak);
        keycloakService.setRealm(REALM);
        userPending = new User();
        userCreated = new User();
        createPendingUser();
        createCreatedUser();
    }

    @Test
    public void testFindUser() {

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search(userPending.getUsername())).thenReturn(getUsersFromKeycloak());

        UserRepresentation keycloakUser = keycloakService.findUser(userPending);

        commonResponseAssertions(keycloakUser);
        verify(usersResource, times(1)).search(userPending.getUsername());
    }

    @Test
    public void testFindUserNotFound() {

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search(userPending.getUsername()))
                .thenReturn(new ArrayList<UserRepresentation>());

        UserRepresentation keycloakUser = keycloakService.findUser(userPending);

        assertNull(keycloakUser);
        verify(usersResource, times(1)).search(userPending.getUsername());
    }

    @Test
    public void testUpdateUser() {

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        List<UserRepresentation> usersList = getUsersFromKeycloak();

        when(usersResource.search(userPending.getUsername())).thenReturn(usersList);
        when(usersResource.get(userPending.getId())).thenReturn(userResource);

        UserRepresentation keycloakUser = keycloakService.updateUser(userCreated, errors);

        commonResponseAssertions(keycloakUser);
        verify(usersResource, times(2)).search(userCreated.getUsername());

    }

    @Test
    public void testUpdateUserNotFound() {

        expectedException.expect(ApplicationValidationException.class);

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        List<UserRepresentation> usersList = new ArrayList<UserRepresentation>();
        when(usersResource.search(userPending.getUsername())).thenReturn(usersList);
        when(usersResource.get(userPending.getId())).thenReturn(null);

        keycloakService.updateUser(userCreated, errors);
    }

    @Test
    public void testCreatePendingStatusUser() {

        UserRepresentation user = getUsersFromKeycloak().get(0);

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(user)).thenReturn(response);

        keycloakService.createUser(userPending);

        verify(usersResource, atLeastOnce()).create(any(UserRepresentation.class));

    }

    @Test
    public void testCreateCreatedStatusUser() {

        UserRepresentation user = getUsersFromKeycloak().get(0);

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(user)).thenReturn(response);

        keycloakService.createUser(userCreated);

        verify(usersResource, atLeastOnce()).create(any(UserRepresentation.class));

    }

    @Test
    public void testChangeUserStatus() {

        List<UserRepresentation> usersList = getUsersFromKeycloak();

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search(userPending.getUsername())).thenReturn(usersList);
        when(usersResource.get(userPending.getId())).thenReturn(userResource);

        UserRepresentation keycloakUser =
                keycloakService.changeUserStatus(userPending.getUsername(), true, errors);

        commonResponseAssertions(keycloakUser);
        verify(userResource, times(1)).update(usersList.get(0));
    }

    @Test
    public void testChangeUserStatusNotFound() {

        expectedException.expect(ApplicationValidationException.class);

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        List<UserRepresentation> usersList = new ArrayList<UserRepresentation>();
        when(usersResource.search(userPending.getUsername())).thenReturn(usersList);

        keycloakService.changeUserStatus(userPending.getUsername(), true, errors);
    }

    @Test
    public void testDeleteUser() {

        when(keycloak.realm(REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        keycloakService.deleteUser(userCreated.getId());

        verify(usersResource, times(1)).delete(userCreated.getId());
    }


    private List<UserRepresentation> getUsersFromKeycloak() {

        UserRepresentation user = new UserRepresentation();
        user.setId(USER_ID);
        user.setUsername(userPending.getUsername());
        user.setEmail(userPending.getEmail());
        List<UserRepresentation> listUsersKeycloack = new ArrayList<>();
        listUsersKeycloack.add(user);

        return listUsersKeycloack;
    }

}
