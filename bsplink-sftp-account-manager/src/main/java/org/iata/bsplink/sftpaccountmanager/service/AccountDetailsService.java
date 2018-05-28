package org.iata.bsplink.sftpaccountmanager.service;

import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;

public interface AccountDetailsService {

    public AccountDetails getAccountDetails(Account account);
}
