package org.iata.bsplink.sftpaccountmanager.system.command;

import org.apache.commons.exec.CommandLine;

/**
 * Executes a system command.
 */
public interface SystemCommandExecutor {

    public void exec(CommandLine command);

    public int exitValue();

    public boolean isFailure();

    public String stringOutput();
}
