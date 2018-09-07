package org.iata.bsplink.filemanager.validation;

import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FileAccessPermissionValidator implements Validator {

    public static final String USER_NOT_FOUND_MSG = "The user was not found";

    private UserService userService;

    public FileAccessPermissionValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FileAccessPermission.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        FileAccessPermission fap = (FileAccessPermission) target;

        if (fap.getUser() != null && !userService.existsUser(fap.getUser())) {
            errors.rejectValue("user", null, USER_NOT_FOUND_MSG);
        }
    }
}
