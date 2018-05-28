package org.iata.bsplink.sftpaccountmanager.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.PUBLIC_KEY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.STATUS;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.junit.Before;
import org.junit.Test;

public class AccountEntityToAccountResponseConverterTest {

    private static final Instant CREATION_TIME = Instant.parse("2018-05-16T01:02:03Z");
    private static final Instant UPDATED_TIME = Instant.parse("2018-05-17T11:12:13Z");

    private AccountEntityToAccountResponseConverter converter;

    @Before
    public void setUp() {

        converter = new AccountEntityToAccountResponseConverter();
    }

    @Test
    public void testConvertsSingleEntity() {

        Account account = getAccountFixture(LOGIN);
        AccountResponse expected = getExpectedAccountResponse(LOGIN);

        assertThat(converter.getResponse(account), equalTo(expected));
    }

    private Account getAccountFixture(String login) {

        Account account = new Account();

        account.setLogin(login);
        account.setPassword("anyPassword");
        account.setPublicKey(PUBLIC_KEY);
        account.setStatus(STATUS);
        account.setCreationTime(CREATION_TIME);
        account.setUpdatedTime(UPDATED_TIME);

        return account;
    }

    private AccountResponse getExpectedAccountResponse(String login) {

        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setLogin(login);
        accountResponse.setPublicKey(PUBLIC_KEY);
        accountResponse.setStatus(STATUS);
        accountResponse.setCreationTime(CREATION_TIME);
        accountResponse.setUpdatedTime(UPDATED_TIME);

        return accountResponse;
    }

    @Test
    public void testConvertsListOfAccounts() {

        List<Account> accounts =
                Arrays.asList(getAccountFixture("login1"), getAccountFixture("login2"));

        List<AccountResponse> expected = Arrays.asList(
                getExpectedAccountResponse("login1"), getExpectedAccountResponse("login2"));

        assertThat(converter.getResponse(accounts), equalTo(expected));
    }

}
