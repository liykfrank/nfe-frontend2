package org.iata.bsplink.sftpaccountmanager.service;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutor;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutorException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * SFTP account management actions based on calls to system commands.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemCommandBasedSftpServerAccountManager implements SftpServerAccountManager {

    private AccountManagementCommandBuilder commandBuilder;
    private SystemCommandExecutor executor;

    /**
     * Instantiates an account manager using a command builder and executor.
     */
    public SystemCommandBasedSftpServerAccountManager(
            AccountManagementCommandBuilder commandBuilder, SystemCommandExecutor executor) {

        this.commandBuilder = commandBuilder;
        this.executor = executor;
    }

    @Override
    public void createAccount(AccountDetails accountDetails) {

        executeCommand(commandBuilder.buildCreateAccountCommand(accountDetails));

        if (accountDetails.getAccount().hasPublicKey()) {

            executeCommand(commandBuilder.buildAddPublicKeyCommand(accountDetails));
        }
    }

    private void executeCommand(CommandLine command) {

        try {

            executor.exec(command);

        } catch (SystemCommandExecutorException exception) {

            throw new RuntimeException("Error executing command: " + command.getExecutable(),
                    exception);
        }

    }

    @Override
    public void updateAccount(AccountDetails accountDetails) {

        executeCommand(commandBuilder.buildUpdateAccountCommand(accountDetails));

        if (accountDetails.getAccount().hasPublicKey()) {

            executeCommand(commandBuilder.buildUpdatePublicKeyCommand(accountDetails));

        } else {

            executeCommand(commandBuilder.buildDeletePublicKeyCommand(accountDetails));
        }
    }


    @Override
    public void deleteAccount(AccountDetails accountDetails) {

        executeCommand(commandBuilder.buildDeleteAccountCommand(accountDetails));
    }

    @Override
    public boolean publicKeyIsValid(String publicKey) {

        CommandLine command = commandBuilder.buildCheckPublicKeyCommand(publicKey);

        try {

            executor.exec(commandBuilder.buildCheckPublicKeyCommand(publicKey));

            // the command will fail if the key is not valid
            return true;

        } catch (SystemCommandExecutorException exception) {

            if (exception.getExitValue() == INCORRECT_PUBLIC_KEY_EXIT) {

                return false;
            }

            throw new RuntimeException("Error executing command: " + command.getExecutable(),
                    exception);
        }
    }

}
