package org.iata.bsplink.sftpaccountmanager.system.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.MODE;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.PUBLIC_KEY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountFixture;
import static org.junit.Assert.assertThat;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.junit.Before;
import org.junit.Test;

public class AccountManagementCommandBuilderTest {

    private Account account;
    private AccountManagementCommandBuilder commandBuilder;

    @Before
    public void setUp() {

        account = getAccountFixture();

        commandBuilder = new AccountManagementCommandBuilder();
    }

    @Test
    public void testBuilsAccountCreationCommand() {

        CommandLine command = commandBuilder.buildCreateAccountCommand(account);

        assertAccountCreationOrUpdateCommand(command);
    }

    private void assertAccountCreationOrUpdateCommand(CommandLine command) {

        assertThat(command.getExecutable(), equalTo("sftp-am-add-update.bash"));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
        assertThat(command.getArguments()[1], equalTo(MODE.toString()));
    }

    @Test
    public void testBuilsAccountUpdateCommand() {

        CommandLine command = commandBuilder.buildUpdateAccountCommand(account);

        assertAccountCreationOrUpdateCommand(command);
    }

    @Test
    public void testBuilsAddPublicKeyCommand() {

        CommandLine command = commandBuilder.buildAddPublicKeyCommand(account);

        assertPublicKeyAdditionOrUpdateCommand(command);
    }

    private void assertPublicKeyAdditionOrUpdateCommand(CommandLine command) {

        assertThat(command.getExecutable(), equalTo("sftp-am-authorize-key.bash"));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
        assertThat(command.getArguments()[1], equalTo("\"" + PUBLIC_KEY + "\""));
    }

    @Test
    public void testBuilsUpdatePublicKeyCommand() {

        CommandLine command = commandBuilder.buildUpdatePublicKeyCommand(account);

        assertPublicKeyAdditionOrUpdateCommand(command);
    }

    @Test
    public void testBuilsAccountDeletionCommand() {

        CommandLine command = commandBuilder.buildDeleteAccountCommand(account);

        assertThat(command.getExecutable(), equalTo("sftp-am-delete.bash"));
        assertThat(command.getArguments()[0], equalTo(LOGIN));
    }
}
