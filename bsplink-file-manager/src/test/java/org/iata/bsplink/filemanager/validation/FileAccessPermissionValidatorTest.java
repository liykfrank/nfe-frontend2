package org.iata.bsplink.filemanager.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class FileAccessPermissionValidatorTest {

    private FileAccessPermissionValidator validator;
    private FileAccessPermission fap;
    private UserService userService;
    private BindingResult errors;

    @Before
    public void setUp() {

        fap = new FileAccessPermission();
        fap.setUser("USER");
        userService = mock(UserService.class);
        errors = new BeanPropertyBindingResult(fap, "fap");
        validator = new FileAccessPermissionValidator(userService);
    }


    @Test
    public void testSupports() {

        assertTrue(validator.supports(FileAccessPermission.class));
        assertFalse(validator.supports(Object.class));
    }


    @Test
    public void testNullUser() {

        fap.setUser(null);
        validator.validate(fap, errors);
        assertFalse(errors.hasErrors());
    }


    @Test
    public void testUser() {

        when(userService.existsUser(fap.getUser())).thenReturn(true);
        validator.validate(fap, errors);
        assertFalse(errors.hasErrors());
    }


    @Test
    public void testUserNotFound() {

        when(userService.existsUser(fap.getUser())).thenReturn(false);
        validator.validate(fap, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        FieldError error = errors.getFieldError();
        assertThat(error.getField(), equalTo("user"));
        assertThat(error.getDefaultMessage(),
                equalTo(FileAccessPermissionValidator.USER_NOT_FOUND_MSG));
    }

}
