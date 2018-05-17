package org.iata.bsplink.sftpaccountmanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.MODE;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.PUBLIC_KEY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountFixture;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

public class SystemCommandBasedSftpServerAccountManagerTest {

    private SystemCommandBasedSftpServerAccountManager accountManager;
    private SystemCommandExecutor executor;
    private Account account;
    private AccountManagementCommandBuilder commandBuilder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {

        executor = mock(SystemCommandExecutor.class);
        commandBuilder = new AccountManagementCommandBuilder();
        accountManager = new SystemCommandBasedSftpServerAccountManager(commandBuilder, executor);
        account = getAccountFixture();
    }

    @Test
    public void testCreatesAccount() {

        accountManager.createAccount(account);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(2)).exec(argument.capture());

        assertCreateOrUpdateAccountCommands(argument.getAllValues());
    }

    private void assertCreateOrUpdateAccountCommands(List<CommandLine> executedCommands) {

        CommandLine createAccountCommand = executedCommands.get(0);

        assertThat(createAccountCommand.getExecutable(), equalTo("sftp-am-add-update.bash"));
        assertThat(createAccountCommand.getArguments()[0], equalTo(LOGIN));
        assertThat(createAccountCommand.getArguments()[1], equalTo(MODE.toString()));

        CommandLine addPublicKeyCommand = executedCommands.get(1);

        assertThat(addPublicKeyCommand.getExecutable(), equalTo("sftp-am-authorize-key.bash"));
        assertThat(addPublicKeyCommand.getArguments()[0], equalTo(LOGIN));
        assertThat(addPublicKeyCommand.getArguments()[1], equalTo("\"" + PUBLIC_KEY + "\""));
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeCreated() {

        thrown.expect(RuntimeException.class);

        when(executor.isFailure()).thenReturn(true);

        accountManager.createAccount(account);
    }

    @Test
    public void testUpdatesAccount() {

        accountManager.updateAccount(account);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(2)).exec(argument.capture());

        assertCreateOrUpdateAccountCommands(argument.getAllValues());
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeUpdated() {

        thrown.expect(RuntimeException.class);

        when(executor.isFailure()).thenReturn(true);

        accountManager.updateAccount(account);
    }

    @Test
    public void testDeletesAccount() {

        accountManager.deleteAccount(account);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(1)).exec(argument.capture());

        CommandLine deleteCommand = argument.getValue();

        assertThat(deleteCommand.getExecutable(), equalTo("sftp-am-delete.bash"));
        assertThat(deleteCommand.getArguments()[0], equalTo(LOGIN));
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeDeleted() {

        thrown.expect(RuntimeException.class);

        when(executor.isFailure()).thenReturn(true);

        accountManager.deleteAccount(account);
    }

}
