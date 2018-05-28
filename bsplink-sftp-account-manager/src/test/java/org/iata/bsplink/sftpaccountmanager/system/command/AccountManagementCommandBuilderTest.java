package org.iata.bsplink.sftpaccountmanager.system.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder.SCRIPT_PUBLIC_KEY_ADD_UPDATE;
import static org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder.SCRIPT_PUBLIC_KEY_CHECK;
import static org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder.SCRIPT_PUBLIC_KEY_DELETE;
import static org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder.SCRIPT_USER_ADD_UPDATE;
import static org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder.SCRIPT_USER_DELETE;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.GROUPS;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.PUBLIC_KEY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.ROOT_DIRECTORY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountDetailsFixture;
import static org.junit.Assert.assertThat;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.junit.Before;
import org.junit.Test;

public class AccountManagementCommandBuilderTest {

    private AccountDetails accountDetails;
    private AccountManagementCommandBuilder commandBuilder;

    @Before
    public void setUp() {

        accountDetails = getAccountDetailsFixture();
        commandBuilder = new AccountManagementCommandBuilder();
    }

    @Test
    public void testBuilsAccountCreationCommand() {

        CommandLine command = commandBuilder.buildCreateAccountCommand(accountDetails);

        assertAccountCreationOrUpdateCommand(command);
    }

    private void assertAccountCreationOrUpdateCommand(CommandLine command) {

        assertThat(command.getExecutable(), equalTo(SCRIPT_USER_ADD_UPDATE));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
        assertThat(command.getArguments()[1], equalTo(ROOT_DIRECTORY));
        assertThat(command.getArguments()[2], equalTo(GROUPS));
    }

    @Test
    public void testBuilsAccountUpdateCommand() {

        CommandLine command = commandBuilder.buildUpdateAccountCommand(accountDetails);

        assertAccountCreationOrUpdateCommand(command);
    }

    @Test
    public void testBuilsAddPublicKeyCommand() {

        CommandLine command = commandBuilder.buildAddPublicKeyCommand(accountDetails);

        assertPublicKeyAdditionOrUpdateCommand(command);
    }

    private void assertPublicKeyAdditionOrUpdateCommand(CommandLine command) {

        assertThat(command.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_ADD_UPDATE));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
        assertThat(command.getArguments()[1], equalTo("\"" + PUBLIC_KEY + "\""));
    }

    @Test
    public void testBuilsUpdatePublicKeyCommand() {

        CommandLine command = commandBuilder.buildUpdatePublicKeyCommand(accountDetails);

        assertPublicKeyAdditionOrUpdateCommand(command);
    }

    @Test
    public void testBuilsAccountDeletionCommand() {

        CommandLine command = commandBuilder.buildDeleteAccountCommand(accountDetails);

        assertThat(command.getExecutable(), equalTo(SCRIPT_USER_DELETE));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
    }

    @Test
    public void testBuilsDeletePublicKeyCommand() {

        CommandLine command = commandBuilder.buildDeletePublicKeyCommand(accountDetails);

        assertThat(command.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_DELETE));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
    }

    @Test
    public void testBuilsCheckPublicKeyCommand() {

        CommandLine command = commandBuilder.buildCheckPublicKeyCommand(PUBLIC_KEY);

        assertThat(command.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_CHECK));
        assertThat(command.getArguments()[0], equalTo("\"" + PUBLIC_KEY + "\""));
    }
}
