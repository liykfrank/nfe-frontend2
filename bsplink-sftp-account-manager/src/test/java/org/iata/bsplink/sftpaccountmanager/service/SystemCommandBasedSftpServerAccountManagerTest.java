package org.iata.bsplink.sftpaccountmanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.iata.bsplink.sftpaccountmanager.service.SftpServerAccountManagerError.INCORRECT_PUBLIC_KEY_EXIT;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.iata.bsplink.sftpaccountmanager.model.AccountDetails;
import org.iata.bsplink.sftpaccountmanager.system.command.AccountManagementCommandBuilder;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutor;
import org.iata.bsplink.sftpaccountmanager.system.command.SystemCommandExecutorException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

public class SystemCommandBasedSftpServerAccountManagerTest {

    private SystemCommandBasedSftpServerAccountManager accountManager;
    private SystemCommandExecutor executor;
    private AccountDetails accountDetails;
    private AccountManagementCommandBuilder commandBuilder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {

        executor = mock(SystemCommandExecutor.class);
        commandBuilder = new AccountManagementCommandBuilder();
        accountManager = new SystemCommandBasedSftpServerAccountManager(commandBuilder, executor);
        accountDetails = getAccountDetailsFixture();
    }

    @Test
    public void testCreatesAccount() throws Exception {

        accountManager.createAccount(accountDetails);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(2)).exec(argument.capture());

        assertCreateOrUpdateAccountCommands(argument.getAllValues());
    }

    private void assertCreateOrUpdateAccountCommands(List<CommandLine> executedCommands) {

        CommandLine createAccountCommand = executedCommands.get(0);

        assertThat(createAccountCommand.getExecutable(), equalTo(SCRIPT_USER_ADD_UPDATE));
        assertThat(createAccountCommand.getArguments()[0], equalTo(LOGIN));
        assertThat(createAccountCommand.getArguments()[1], equalTo(ROOT_DIRECTORY));
        assertThat(createAccountCommand.getArguments()[2], equalTo(GROUPS));

        CommandLine addPublicKeyCommand = executedCommands.get(1);

        assertThat(addPublicKeyCommand.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_ADD_UPDATE));
        assertThat(addPublicKeyCommand.getArguments()[0], equalTo(LOGIN));
        assertThat(addPublicKeyCommand.getArguments()[1], equalTo("\"" + PUBLIC_KEY + "\""));
    }

    @Test
    public void testDoesNotAddPublicKeyOnCreationIfNotDefined() throws Exception {

        accountDetails.getAccount().setPublicKey(null);

        accountManager.createAccount(accountDetails);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(1)).exec(argument.capture());

        CommandLine executedCommand = argument.getAllValues().get(0);

        assertThat(executedCommand.getExecutable(), not(equalTo(SCRIPT_PUBLIC_KEY_ADD_UPDATE)));
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeCreated() throws Exception {

        thrown.expect(RuntimeException.class);

        configureExecutorToThrowException();

        accountManager.createAccount(accountDetails);
    }

    private void configureExecutorToThrowException() throws Exception {

        doThrow(mock(SystemCommandExecutorException.class)).when(executor)
                .exec(any(CommandLine.class));
    }

    @Test
    public void testUpdatesAccount() throws Exception {

        accountManager.updateAccount(accountDetails);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(2)).exec(argument.capture());

        assertCreateOrUpdateAccountCommands(argument.getAllValues());
    }

    @Test
    public void testDeletesPublicKeyOnUpdateIfNotDefined() throws Exception {

        accountDetails.getAccount().setPublicKey(null);

        accountManager.updateAccount(accountDetails);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(2)).exec(argument.capture());

        CommandLine executedCommand = argument.getAllValues().get(1);

        assertThat(executedCommand.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_DELETE));
        assertThat(executedCommand.getArguments()[0], equalTo(LOGIN));
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeUpdated() throws Exception {

        thrown.expect(RuntimeException.class);

        configureExecutorToThrowException();

        accountManager.updateAccount(accountDetails);
    }

    @Test
    public void testDeletesAccount() throws Exception {

        accountManager.deleteAccount(accountDetails);

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(1)).exec(argument.capture());

        CommandLine deleteCommand = argument.getValue();

        assertThat(deleteCommand.getExecutable(), equalTo(SCRIPT_USER_DELETE));
        assertThat(deleteCommand.getArguments()[0], equalTo(LOGIN));
    }

    @Test
    public void testThrowsExceptionIfAccountCanNotBeDeleted() throws Exception {

        thrown.expect(RuntimeException.class);

        configureExecutorToThrowException();

        accountManager.deleteAccount(accountDetails);
    }

    @Test
    public void testValidatesValidPublicKey() throws Exception {

        assertThat(accountManager.publicKeyIsValid(PUBLIC_KEY), equalTo(true));

        ArgumentCaptor<CommandLine> argument = ArgumentCaptor.forClass(CommandLine.class);
        verify(executor, times(1)).exec(argument.capture());

        CommandLine validateCommand = argument.getValue();

        assertThat(validateCommand.getExecutable(), equalTo(SCRIPT_PUBLIC_KEY_CHECK));
        assertThat(validateCommand.getArguments()[0], equalTo("\"" + PUBLIC_KEY + "\""));
    }

    @Test
    public void testThrowsExceptionIfPublicKeyCanNotBeValidated() throws Exception {

        thrown.expect(RuntimeException.class);

        configureExecutorToThrowException();

        accountManager.publicKeyIsValid(PUBLIC_KEY);
    }

    @Test
    public void testValidatesInvalidPublicKey() throws Exception {

        SystemCommandExecutorException exception = new SystemCommandExecutorException(
                SCRIPT_PUBLIC_KEY_CHECK, INCORRECT_PUBLIC_KEY_EXIT, null, new Exception());

        doThrow(exception).when(executor).exec(any(CommandLine.class));

        assertThat(accountManager.publicKeyIsValid(PUBLIC_KEY), equalTo(false));
    }

}
