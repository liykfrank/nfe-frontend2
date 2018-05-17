package org.iata.bsplink.sftpaccountmanager.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.model.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private SftpServerAccountManager sftpServerAccountManager;
    private PasswordInitializer passwordInitializer;

    /**
     * Instantiates the account service.
     */
    public AccountService(AccountRepository accountRepository,
            SftpServerAccountManager sftpServerAccountManager,
            PasswordInitializer passwordInitializer) {

        this.accountRepository = accountRepository;
        this.sftpServerAccountManager = sftpServerAccountManager;
        this.passwordInitializer = passwordInitializer;
    }

    /**
     * Retrieves a list of SFTP accounts.
     */
    public List<Account> findAll() {

        return accountRepository.findAll();
    }

    /**
     * Retrieves an SFTP account by its login.
     */
    public Optional<Account> findByLogin(String login) {

        return accountRepository.findById(login);
    }

    /**
     * Creates a new SFTP account.
     */
    @Transactional
    public Account create(AccountRequest accountRequest) {

        Account account = new Account();
        BeanUtils.copyProperties(accountRequest, account);

        account.setPassword(passwordInitializer.generateNewPasswordHash());

        Account savedAccount = accountRepository.save(account);

        sftpServerAccountManager.createAccount(savedAccount);

        return savedAccount;
    }

    public boolean loginExists(String login) {

        return findByLogin(login).isPresent();
    }

    /**
     * Updates an SFTP account.
     *
     * <p>
     * Returns an Optional&ltAccount&gt which can be empty if the login doesn't exist.
     * </p>
     */
    public Optional<Account> update(String login, AccountRequest accountRequest) {

        Optional<Account> optionalSavedAccount = findByLogin(login);

        if (!optionalSavedAccount.isPresent()) {

            return optionalSavedAccount;
        }

        BeanUtils.copyProperties(accountRequest, optionalSavedAccount.get(), "login");

        accountRepository.save(optionalSavedAccount.get());
        sftpServerAccountManager.updateAccount(optionalSavedAccount.get());

        return optionalSavedAccount;
    }

    /**
     * Deletes an SFTP account.
     *
     * <p>
     * Returns an Optional&ltAccount&gt which can be empty if the login doesn't exist.
     * </p>
     */
    public Optional<Account> delete(String login) {

        Optional<Account> optionalSavedAccount = findByLogin(login);

        if (!optionalSavedAccount.isPresent()) {

            return optionalSavedAccount;
        }

        accountRepository.delete(optionalSavedAccount.get());
        sftpServerAccountManager.deleteAccount(optionalSavedAccount.get());

        return optionalSavedAccount;
    }

    /**
     * Saves an SFTP account.
     */
    public Account save(Account account) {

        return accountRepository.save(account);
    }

}
