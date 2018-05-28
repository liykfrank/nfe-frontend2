package org.iata.bsplink.sftpaccountmanager.dto;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AccountResponse {

    @NonNull
    private String login;

    @NonNull
    private AccountStatus status = AccountStatus.ENABLED;

    @NonNull
    private Instant creationTime;

    @NonNull
    private Instant updatedTime;

    private String publicKey;
}
