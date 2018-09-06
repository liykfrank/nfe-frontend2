package org.iata.bsplink.user.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.iata.bsplink.user.model.entity.Address;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserStatus;
import org.iata.bsplink.user.model.entity.UserType;
import org.keycloak.representations.idm.UserRepresentation;

public abstract class BaseUserTest {

    protected static final String GET_USER_URL = "/v1/users/{id}";
    protected static final String CREATE_USER_URL = "/v1/users";
    protected static final String UPDATE_USER_URL = "/v1/users/{id}";
    protected static final String DELETE_USER_URL = "/v1/users/{id}";

    protected static final String USER_ID = "b348f80e-59sa-4a2f-9ci4-879805d92920";
    protected static final String USER_ID_NOT_EXISTS = "wrong_id";
    protected static final String REALM = "realm";

    protected User userPending;
    protected User userCreated;

    protected User getBaseUser() {

        User user = new User();
        user.setId(USER_ID);
        user.setEmail("email@email.com");
        user.setUsername("email@email.com");
        user.setName("Test");
        user.setLastName("Test");
        user.setUserType(UserType.AIRLINE);
        user.setUserCode("code");
        user.setTelephone("654987321");
        user.setOrganization("Organization");

        Address address = new Address();
        address.setDescription("Direction");
        address.setLocality("Locality");
        address.setCity("City");
        address.setZip("Zip");
        address.setCountry("Country");

        user.setAddress(address);

        return user;
    }


    /**
     * Creates the user for the tests.
     */
    protected void initiatePendingUser() {

        userPending = getBaseUser();
        userPending.setStatus(UserStatus.PENDING);
    }

    /**
     * Creates the user for the tests.
     */
    protected void initiateCreatedUser() {

        userCreated = getBaseUser();
        userCreated.setStatus(UserStatus.CREATED);

    }

    /**
     * Common response assertions.
     *
     * @param response the response that is going to be checked
     */
    protected void commonResponseAssertions(User user, User userReturned) {
        assertNotNull(userReturned);
        assertEquals(user.getId(), userReturned.getId());
        assertEquals(user.getUsername(), userReturned.getUsername());
        assertEquals(user.getUserCode(), userReturned.getUserCode());
        assertEquals(user.getEmail(), userReturned.getEmail());
        assertEquals(user.getTemplates(), userReturned.getTemplates());
    }

    /**
     * Common keycloak response assertions.
     *
     * @param keycloakUser UserRepresentation
     */
    public void commonResponseAssertions(UserRepresentation keycloakUser) {
        assertNotNull(keycloakUser);
        assertEquals(userPending.getUsername(), keycloakUser.getUsername());
        assertEquals(userPending.getId(), keycloakUser.getId());
    }

}
