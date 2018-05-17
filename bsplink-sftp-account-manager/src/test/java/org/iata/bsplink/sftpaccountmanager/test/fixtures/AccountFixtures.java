package org.iata.bsplink.sftpaccountmanager.test.fixtures;

import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountMode;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;

public class AccountFixtures {

    public static final String LOGIN = "login1";
    public static final AccountMode MODE = AccountMode.RO;
    public static final AccountStatus STATUS = AccountStatus.ENABLED;
    public static final String PUBLIC_KEY = "any Public Key";

    /**
     * Creates an Account fixture.
     */
    public static Account getAccountFixture() {

        Account account = new Account(LOGIN, MODE);
        account.setPublicKey(PUBLIC_KEY);

        return account;
    }

    /**
     * Creates an AccountRequest fixture.
     */
    public static AccountRequest getAccountRequestFixture() {

        AccountRequest account = new AccountRequest(LOGIN, MODE, STATUS);
        account.setPublicKey(PUBLIC_KEY);

        return account;
    }

}
