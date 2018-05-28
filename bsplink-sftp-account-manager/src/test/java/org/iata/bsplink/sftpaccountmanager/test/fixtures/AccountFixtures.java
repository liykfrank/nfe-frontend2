package org.iata.bsplink.sftpaccountmanager.test.fixtures;

import java.util.Arrays;

import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;

public class AccountFixtures {

    public static final String LOGIN = "login1";
    public static final AccountStatus STATUS = AccountStatus.ENABLED;
    public static final String PUBLIC_KEY = "any Public Key";
    public static final String GROUPS = "group1,group2,group3";
    public static final String ROOT_DIRECTORY = "rootDirectory";

    /**
     * Creates an Account fixture.
     */
    public static Account getAccountFixture() {

        Account account = new Account(LOGIN);
        account.setPublicKey(PUBLIC_KEY);

        return account;
    }

    /**
     * Creates an AccountRequest fixture.
     */
    public static AccountRequest getAccountRequestFixture() {

        AccountRequest account = new AccountRequest(LOGIN, STATUS);
        account.setPublicKey(PUBLIC_KEY);

        return account;
    }

    /**
     * Creates an AccountDetails fixture.
     */
    public static AccountDetails getAccountDetailsFixture() {

        return getAccountDetailsFixture(getAccountFixture());
    }

    /**
     * Creates an AccountDetails fixture for a specific account.
     */
    public static AccountDetails getAccountDetailsFixture(Account account) {

        return new AccountDetails(account, ROOT_DIRECTORY, Arrays.asList(GROUPS.split(",")));
    }
}
