package org.iata.bsplink.sftpaccountmanager.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.iata.bsplink.commons.rest.exception.ApplicationConflictException;
import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.sftpaccountmanager.dto.AccountEntityToAccountResponseConverter;
import org.iata.bsplink.sftpaccountmanager.dto.AccountPasswordRequest;
import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.dto.AccountResponse;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountEntityToAccountResponseConverter responseConverter;

    /**
     * Gets a list of SFTP accounts.
     */
    @GetMapping()
    public ResponseEntity<List<AccountResponse>> getAccount() {

        return new ResponseEntity<>(responseConverter.getResponse(accountService.findAll()),
                HttpStatus.OK);
    }

    /**
     * Gets an SFTP account.
     */
    @GetMapping("/{login}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("login") Account account) {

        if (account == null) {

            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(responseConverter.getResponse(account), HttpStatus.OK);
    }

    /**
     * Creates a new SFTP account.
     */
    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest accountRequest, Errors errors) {

        throwApplicationValidationExceptionIfThereAreErrors(errors);

        if (accountService.loginExists(accountRequest.getLogin())) {

            throw new ApplicationConflictException(
                    String.format("Login name %s exists already", accountRequest.getLogin()));
        }

        return new ResponseEntity<>(
                responseConverter.getResponse(accountService.create(accountRequest)),
                HttpStatus.CREATED);
    }

    private void throwApplicationValidationExceptionIfThereAreErrors(Errors errors) {

        if (errors.hasErrors()) {

            throw new ApplicationValidationException(errors);
        }
    }

    /**
     * Updates an SFTP account.
     */
    @PutMapping("/{login}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable String login,
            @Valid @RequestBody AccountRequest accountRequest, Errors errors) {

        throwApplicationValidationExceptionIfThereAreErrors(errors);

        Optional<Account> optionalSavedAccount = accountService.update(login, accountRequest);

        if (!optionalSavedAccount.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(responseConverter.getResponse(optionalSavedAccount.get()),
                HttpStatus.OK);
    }

    /**
     * Deletes an SFTP account.
     */
    @DeleteMapping("/{login}")
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable String login) {

        Optional<Account> optionalSavedAccount = accountService.delete(login);

        if (!optionalSavedAccount.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(responseConverter.getResponse(optionalSavedAccount.get()),
                HttpStatus.OK);
    }

    /**
     * Updates an SFTP password.
     */
    @PutMapping("/{login}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("login") Account account,
            @Valid @RequestBody AccountPasswordRequest accountPasswordRequest, Errors errors) {

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        if (!passwordEncoder.matches(accountPasswordRequest.getOldPassword(),
                account.getPassword())) {

            throw new ApplicationException("Incorrect password", HttpStatus.FORBIDDEN);
        }

        account.setPassword(passwordEncoder.encode(accountPasswordRequest.getPassword()));
        accountService.save(account);

        return ResponseEntity.ok().build();
    }
}
