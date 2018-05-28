package org.iata.bsplink.sftpaccountmanager.service;

import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;

/**
 * Account management actions that can be done on the underlying SFTP server system.
 */
public interface SftpServerAccountManager {

    public static final int INCORRECT_PUBLIC_KEY_EXIT = 80;

    public void createAccount(AccountDetails accountDetails);

    public void updateAccount(AccountDetails accountDetails);

    public void deleteAccount(AccountDetails accountDetails);

    public boolean publicKeyIsValid(String publicKey);
}
