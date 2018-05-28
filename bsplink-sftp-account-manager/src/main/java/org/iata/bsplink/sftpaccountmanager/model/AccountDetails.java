package org.iata.bsplink.sftpaccountmanager.model;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;

/**
 * Contains additional information of the sftp account.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class AccountDetails {

    @NonNull
    private Account account;

    /**
     * Sftp account root directory.
     */
    @NonNull
    private String accountRootDirectory;

    /**
     * List of sftp user's groups.
     *
     * <p>
     * The sftp user will belong to all the groups listed here.
     * </p>
     */
    @NonNull
    private List<String> accountGroups;
}
