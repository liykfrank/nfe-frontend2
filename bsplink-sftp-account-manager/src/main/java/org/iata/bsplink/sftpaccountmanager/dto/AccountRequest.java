package org.iata.bsplink.sftpaccountmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.iata.bsplink.sftpaccountmanager.model.entity.AccountMode;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;

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
    private AccountMode mode;

    @NonNull
    private AccountStatus status;

    private String publicKey;
}

