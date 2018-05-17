package org.iata.bsplink.sftpaccountmanager.system.command;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.springframework.stereotype.Component;

/**
 * Creates the account management commands that should be executed in the system.
 */
@Component
public class AccountManagementCommandBuilder {

    /**
     * Creates the command to create a new account.
     */
    public CommandLine buildCreateAccountCommand(Account account) {

        return buildCreateAndUpdateCommand(account);
    }

    private CommandLine buildCreateAndUpdateCommand(Account account) {

        CommandLine command = new CommandLine("sftp-am-add-update.bash");

        command.addArgument(account.getLogin());
        command.addArgument(account.getMode().toString());

        return command;
    }

    /**
     * Creates the command to update an account.
     */
    public CommandLine buildUpdateAccountCommand(Account account) {

        return buildCreateAndUpdateCommand(account);
    }

    /**
     * Creates the command that adds the user's public key.
     */
    public CommandLine buildAddPublicKeyCommand(Account account) {

        return buildAddAndUpdatePublicKeyCommand(account);
    }

    private CommandLine buildAddAndUpdatePublicKeyCommand(Account account) {

        CommandLine command = new CommandLine("sftp-am-authorize-key.bash");

        command.addArgument(account.getLogin());
        command.addArgument(account.getPublicKey(), true);

        return command;
    }

    /**
     * Creates the command that updates the user's public key.
     */
    public CommandLine buildUpdatePublicKeyCommand(Account account) {

        return buildAddAndUpdatePublicKeyCommand(account);
    }

    /**
     * Creates the command that deletes an sftp account.
     */
    public CommandLine buildDeleteAccountCommand(Account account) {

        CommandLine command = new CommandLine("sftp-am-delete.bash");

        command.addArgument(account.getLogin());

        return command;
    }

}
