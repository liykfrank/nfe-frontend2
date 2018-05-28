package org.iata.bsplink.sftpaccountmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;
import org.iata.bsplink.sftpaccountmanager.validation.constraints.PublicKeyConstraint;

/**
 * DTO that contains the editable data of SFTP accounts.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AccountRequest {

    @NonNull
    private String login;

    @NonNull
    private AccountStatus status;

    @PublicKeyConstraint(emptyIsValid = true)
    private String publicKey;
}

