package org.iata.bsplink.sftpaccountmanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountDetailsFixture;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountFixture;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountRequestFixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;
import org.iata.bsplink.sftpaccountmanager.model.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;

public class AccountServiceTest {

    private static final String PASSWORD_HASH = "fakePasswordHash";

    private Account account;
    private AccountDetails accountDetails;
    private AccountRequest accountRequest;
    private AccountService accountService;
    private AccountRepository accountRepository;
    private SftpServerAccountManager sftpServerAccountManager;
    private PasswordInitializer passwordInitializer;
    private AccountDetailsService accountDetailsService;

    @Before
    public void setUp() {

        account = getAccountFixture();
        account.setPassword(PASSWORD_HASH);

        accountRequest = getAccountRequestFixture();

        accountRepository = mock(AccountRepository.class);
        when(accountRepository.save(isA(Account.class))).thenAnswer(returnsFirstArg());

        sftpServerAccountManager = mock(SftpServerAccountManager.class);

        passwordInitializer = mock(PasswordInitializer.class);
        when(passwordInitializer.generateNewPasswordHash()).thenReturn(PASSWORD_HASH);


        accountDetails = getAccountDetailsFixture(account);
        accountDetailsService = mock(AccountDetailsService.class);
        when(accountDetailsService.getAccountDetails(account)).thenReturn(accountDetails);

        accountService = new AccountService(accountRepository, sftpServerAccountManager,
                passwordInitializer, accountDetailsService);
    }

    @Test
    public void testCreatesAccount() {

        accountService.create(accountRequest);

        verify(accountRepository).save(account);
        verify(sftpServerAccountManager).createAccount(accountDetails);
    }

    @Test
    public void testInitializesPasswordOnAccountCreation() {

        Account savedAccount = accountService.create(accountRequest);

        assertThat(savedAccount.getPassword(), equalTo(PASSWORD_HASH));
    }

    @Test
    public void testAccountIsCreatedEnabled() {

        Account savedAccount = accountService.create(accountRequest);

        assertThat(savedAccount.getStatus(), equalTo(AccountStatus.ENABLED));
    }

    @Test
    public void testIsAwareOfExistentAccounts() {

        assertFalse(accountService.loginExists(LOGIN));

        configureRepositorySearchMock();

        assertTrue(accountService.loginExists(LOGIN));
    }

    private void configureRepositorySearchMock() {

        configureRepositorySearchMock(true);
    }

    private void configureRepositorySearchMock(boolean accountExists) {

        Optional<Account> optionalAccount = accountExists
                ? Optional.of(account)
                : Optional.empty();

        when(accountRepository.findById(LOGIN)).thenReturn(optionalAccount);
    }

    @Test
    public void testIsAwareOfInexistentAccountsOnUpdate() {

        configureRepositorySearchMock(false);

        assertFalse(accountService.update(accountRequest.getLogin(), accountRequest).isPresent());
    }

    @Test
    public void testUpdatesAccount() {

        configureRepositorySearchMock();

        Optional<Account> optionalUpdatedAccount =
                accountService.update(accountRequest.getLogin(), getAccountRequestFixture());

        verify(accountRepository).save(account);
        verify(sftpServerAccountManager).updateAccount(accountDetails);

        assertTrue(optionalUpdatedAccount.isPresent());
        assertThat(optionalUpdatedAccount.get(), sameInstance(account));
    }

    @Test
    public void testOnlyEditablePropertiesAreSetOnUpdate() {

        configureRepositorySearchMock();

        accountRequest.setLogin("foo");

        Account updatedAccount = accountService.update(LOGIN, accountRequest).get();

        assertEquals(LOGIN, updatedAccount.getLogin());
        assertNull(updatedAccount.getCreationTime());
        assertNull(updatedAccount.getUpdatedTime());
    }

    @Test
    public void testDeletesAccount() {

        configureRepositorySearchMock();

        accountService.delete(LOGIN);

        verify(accountRepository).delete(account);
        verify(sftpServerAccountManager).deleteAccount(accountDetails);
    }

    @Test
    public void testIsAwareOfInexistentAccountsOnDelete() {

        configureRepositorySearchMock(false);

        assertFalse(accountService.delete(LOGIN).isPresent());
    }

    @Test
    public void testGetsAccountByLogin() {

        configureRepositorySearchMock();

        Optional<Account> optionalAccount = accountService.findByLogin(LOGIN);

        assertTrue(optionalAccount.isPresent());
        assertThat(optionalAccount.get(), sameInstance(account));
    }

    @Test
    public void testIsAwareOfInexistentAccountsOnLookup() {

        configureRepositorySearchMock(false);

        Optional<Account> optionalAccount = accountService.findByLogin(LOGIN);

        assertFalse(optionalAccount.isPresent());
    }

    @Test
    public void testRetrievesAccounts() throws Exception {

        List<Account> accounts = new ArrayList<>();

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> retrievedAccounts = accountService.findAll();

        assertThat(retrievedAccounts, sameInstance(accounts));
    }

    @Test
    public void testSavesAccount() {

        Account savedAccount = accountService.save(account);

        verify(accountRepository).save(account);

        assertThat(savedAccount, sameInstance(account));
    }
}
