package org.iata.bsplink.sftpaccountmanager.system.command;

import org.apache.commons.exec.CommandLine;

/**
 * Executes a system command.
 */
public interface SystemCommandExecutor {

    public void exec(CommandLine command) throws SystemCommandExecutorException;

    public int exitValue();

    public String stringOutput();
}
