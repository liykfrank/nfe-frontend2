package org.iata.bsplink.sftpaccountmanager.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.iata.bsplink.sftpaccountmanager.validation.constraints.PasswordMatchConstraint;

@Data
@NoArgsConstructor
@PasswordMatchConstraint
public class AccountPasswordRequest {

    @NotNull
    private String oldPassword;

    @Size(min = 8, message = "must be at least 8 characters length")
    private String password;

    @NotNull
    private String confirmPassword;

    /**
     * Creates an AccountPasswordRequest.
     */
    public AccountPasswordRequest(String oldPassword, String password, String confirmPassword) {

        this.oldPassword = oldPassword;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
