package org.iata.bsplink.service;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.service.UserServiceImpl;
import org.iata.bsplink.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
public class UserServiceImplTest extends BaseUserTest {

    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Errors errors;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        this.userService = new UserServiceImpl(userRepository);
        user = new User();
        createUser();
    }

    /**
     * Get user test.
     */
    @Test
    public void testGetUser() {

        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);

        user = userService.getUser(USER_ID).get();

        commonResponseAssertions(user);

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
     * Create user test.
     */
    @Test
    public void testCreateUser() {

        doReturn(user).when(userRepository).save(any(User.class));

        user = userService.createUser(user, null);

        commonResponseAssertions(user);

        verify(userRepository, times(1)).save(any(User.class));
    }


    /**
     * Create user already exists.
     */
    @Test
    public void testCreateUserAlreadyExistsWithId() {

        expectedException.expect(ApplicationValidationException.class);

        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);

        user = userService.createUser(user, errors);

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Create user already exists.
     */
    @Test
    public void testCreateUserAlreadyExistsWithUsername() {

        expectedException.expect(ApplicationValidationException.class);

        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());

        user = userService.createUser(user, errors);

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Update user test.
     */
    @Test
    public void testUpdateUser() {

        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doReturn(user).when(userRepository).save(any(User.class));

        user = userService.updateUser(user, user);

        commonResponseAssertions(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Delete user test.
     */
    @Test
    public void testDeleteUser() {

        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(user);

        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Delete user not found test.
     */
    @Test
    public void testDeleteUserNotFound() {

        doReturn(Optional.empty()).when(userRepository).findById(USER_ID);

        userService.deleteUser(user);
    }

}
