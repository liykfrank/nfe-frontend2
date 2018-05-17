package org.iata.bsplink.sftpaccountmanager.service;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutor;
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
    public void createAccount(Account account) {

        executeCommand(commandBuilder.buildCreateAccountCommand(account));
        executeCommand(commandBuilder.buildAddPublicKeyCommand(account));
    }

    private void executeCommand(CommandLine command) {

        executor.exec(command);

        if (executor.isFailure()) {

            throw new RuntimeException(
                    "Command execution error: " + command + " output: " + executor.stringOutput());
        }
    }

    @Override
    public void updateAccount(Account account) {

        executeCommand(commandBuilder.buildUpdateAccountCommand(account));
        executeCommand(commandBuilder.buildUpdatePublicKeyCommand(account));
    }


    @Override
    public void deleteAccount(Account account) {

        executeCommand(commandBuilder.buildDeleteAccountCommand(account));
    }

}
