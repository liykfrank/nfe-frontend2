package org.iata.bsplink.filemanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iata.bsplink.filemanager.exception.FeignClientException;
import org.iata.bsplink.filemanager.pojo.User;
import org.iata.bsplink.filemanager.restclient.UserClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class UserServiceTest {

    private UserClient client;
    private UserService service;

    @Before
    public void setUp() {
        client = mock(UserClient.class);
        service = new UserService(client);
    }

    @Test
    public void testFindUser() {
        User user = new User();
        user.setId("USER");

        when(client.findUser(user.getId())).thenReturn(ResponseEntity.ok(user));

        User userFound = service.findUser(user.getId());

        assertThat(userFound, equalTo(userFound));
        verify(client).findUser(user.getId());
    }


    @Test
    public void testNotFindUser() {
        String notFound = "notFound";
        when(client.findUser(notFound)).thenReturn(ResponseEntity.notFound().build());

        User userFound = service.findUser(notFound);

        assertNull(userFound);
        verify(client).findUser(notFound);
    }


    @Test(expected = FeignClientException.class)
    public void testFindUserError() {
        String notFound = "notFound";
        when(client.findUser(notFound)).thenReturn(ResponseEntity.badRequest().build());

        service.findUser(notFound);
    }


    @Test
    public void testExistsUser() {
        User user = new User();
        user.setId("USER");

        when(client.findUser(user.getId())).thenReturn(ResponseEntity.ok(user));

        assertTrue(service.existsUser(user.getId()));
        verify(client).findUser(user.getId());
    }


    @Test
    public void testNotExistsUser() {
        String notFound = "notFound";
        when(client.findUser(notFound)).thenReturn(ResponseEntity.ok(null));

        assertFalse(service.existsUser(notFound));
        verify(client).findUser(notFound);
    }
}
