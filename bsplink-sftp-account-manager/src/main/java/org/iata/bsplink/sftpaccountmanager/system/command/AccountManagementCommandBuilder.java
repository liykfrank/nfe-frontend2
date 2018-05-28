package org.iata.bsplink.sftpaccountmanager.system.command;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.springframework.stereotype.Component;

/**
 * Creates the account management commands that should be executed in the system.
 */
@Component
public class AccountManagementCommandBuilder {

    public static final String SCRIPT_USER_ADD_UPDATE = "sftp-am-user-add-update.bash";
    public static final String SCRIPT_USER_DELETE = "sftp-am-user-delete.bash";
    public static final String SCRIPT_PUBLIC_KEY_ADD_UPDATE = "sftp-am-authorize-key-set.bash";
    public static final String SCRIPT_PUBLIC_KEY_CHECK = "sftp-am-authorize-key-check.bash";
    public static final String SCRIPT_PUBLIC_KEY_DELETE = "sftp-am-authorize-key-remove.bash";

    /**
     * Creates the command to create a new account.
     */
    public CommandLine buildCreateAccountCommand(AccountDetails accountDetails) {

        return buildCreateAndUpdateCommand(accountDetails);
    }

    private CommandLine buildCreateAndUpdateCommand(AccountDetails accountDetails) {

        CommandLine command = new CommandLine(SCRIPT_USER_ADD_UPDATE);

        command.addArgument(accountDetails.getAccount().getLogin());
        command.addArgument(accountDetails.getAccountRootDirectory());
        command.addArgument(String.join(",", accountDetails.getAccountGroups()));

        return command;
    }

    /**
     * Creates the command to update an account.
     */
    public CommandLine buildUpdateAccountCommand(AccountDetails accountDetails) {

        return buildCreateAndUpdateCommand(accountDetails);
    }

    /**
     * Creates the command that adds the user's public key.
     */
    public CommandLine buildAddPublicKeyCommand(AccountDetails accountDetails) {

        return buildAddAndUpdatePublicKeyCommand(accountDetails);
    }

    private CommandLine buildAddAndUpdatePublicKeyCommand(AccountDetails accountDetails) {

        CommandLine command = new CommandLine(SCRIPT_PUBLIC_KEY_ADD_UPDATE);

        command.addArgument(accountDetails.getAccount().getLogin());
        command.addArgument(accountDetails.getAccount().getPublicKey(), true);

        return command;
    }

    /**
     * Creates the command that updates the user's public key.
     */
    public CommandLine buildUpdatePublicKeyCommand(AccountDetails accountDetails) {

        return buildAddAndUpdatePublicKeyCommand(accountDetails);
    }

    /**
     * Creates the command that deletes an sftp account.
     */
    public CommandLine buildDeleteAccountCommand(AccountDetails accountDetails) {

        CommandLine command = new CommandLine(SCRIPT_USER_DELETE);

        command.addArgument(accountDetails.getAccount().getLogin());

        return command;
    }

    /**
     * Creates the command that checks the validity of a public key.
     */
    public CommandLine buildCheckPublicKeyCommand(String publicKey) {

        CommandLine command = new CommandLine(SCRIPT_PUBLIC_KEY_CHECK);

        command.addArgument(publicKey);

        return command;
    }

    /**
     * Creates the command that deletes a public key.
     */
    public CommandLine buildDeletePublicKeyCommand(AccountDetails accountDetails) {

        CommandLine command = new CommandLine(SCRIPT_PUBLIC_KEY_DELETE);

        command.addArgument(accountDetails.getAccount().getLogin());

        return command;
    }

}
