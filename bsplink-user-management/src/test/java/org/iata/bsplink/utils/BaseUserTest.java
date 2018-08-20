package org.iata.bsplink.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.iata.bsplink.user.model.entity.Address;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserType;

public abstract class BaseUserTest {

    protected static final String GET_USER_URL = "/v1/users/{id}";
    protected static final String CREATE_USER_URL = "/v1/users";
    protected static final String UPDATE_USER_URL = "/v1/users/{id}";
    protected static final String DELETE_USER_URL = "/v1/users/{id}";

    protected static final String USER_ID = "b348f80e-59sa-4a2f-9ci4-879805d92920";

    protected User user;

    /**
     * Creates the user for the tests.
     */
    protected void createUser() {

        user = new User();
        user.setId(USER_ID);
        user.setUsername("bsplink.test");
        user.setName("Test");
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
    }

    /**
     * Common response assertions.
     *
     * @param response the response that is going to be checked
     */
    protected void commonResponseAssertions(User user) {

        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
    }

}
