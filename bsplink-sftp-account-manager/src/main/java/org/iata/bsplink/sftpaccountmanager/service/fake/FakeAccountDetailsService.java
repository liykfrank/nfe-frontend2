package org.iata.bsplink.sftpaccountmanager.service.fake;

import java.util.ArrayList;
import java.util.Optional;

import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.service.AccountDetailsService;
import org.iata.bsplink.sftpaccountmanager.service.fake.FakeAccountDetailsServiceConfiguration.Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Fake account details service.
 *
 * <p>
 * The AccountDetailsService is an external service that doesn't exist yet. This
 * service fakes the functionality until the real service is available.
 * </p>
 */
@Service
public class FakeAccountDetailsService implements AccountDetailsService {

    @Autowired
    private FakeAccountDetailsServiceConfiguration configuration;

    @Override
    public AccountDetails getAccountDetails(Account account) {

        Optional<Details> optionalDetails = findDetailsForAccount(account);

        AccountDetails accountDetails =
                new AccountDetails(account, "/", new ArrayList<>());

        if (optionalDetails.isPresent()) {

            accountDetails.setAccountRootDirectory(optionalDetails.get().getDirectory());
            accountDetails.setAccountGroups(optionalDetails.get().getGroups());
        }

        return accountDetails;
    }

    private Optional<Details> findDetailsForAccount(Account account) {

        String login = account.getLogin();

        return configuration.getAccounts().stream()
                .filter(x -> x.getLogin().equals(login))
                .findFirst();
    }

}
