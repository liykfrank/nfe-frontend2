package org.iata.bsplink.sftpaccountmanager.service;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;

/**
 * Account management actions that can be done on the underlying SFTP server system.
 */
public interface SftpServerAccountManager {

    public void createAccount(Account account);

    public void updateAccount(Account account);

    public void deleteAccount(Account account);
}
