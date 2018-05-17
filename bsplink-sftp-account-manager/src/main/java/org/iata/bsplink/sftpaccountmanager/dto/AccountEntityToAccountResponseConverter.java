package org.iata.bsplink.sftpaccountmanager.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Converts Account entities to AccountResponse DTOs.
 *
 * <p>
 * The Account entity has sensitive data that can't be send as response, this
 * converter narrows the entity content to the minimal data necessary.
 * </p>
 */
@Component
public class AccountEntityToAccountResponseConverter {

    /**
     * Converts a single account.
     */
    public AccountResponse getResponse(Account account) {

        AccountResponse accountResponse = new AccountResponse();

        BeanUtils.copyProperties(account, accountResponse);

        return accountResponse;
    }

    /**
     * Converts a list of accounts.
     */
    public List<AccountResponse> getResponse(List<Account> accounts) {

        return accounts.stream().map(account -> {

            AccountResponse accountResponse = new AccountResponse();
            BeanUtils.copyProperties(account, accountResponse);

            return accountResponse;

        }).collect(Collectors.toList());
    }
}
