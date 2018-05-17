package org.iata.bsplink.sftpaccountmanager.system.command;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.exec.CommandLine;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * SystemCommandExecutor implementation that does nothing but log the command execution.
 */
@Component
@Profile({ "dev", "test" })
@CommonsLog
public class LoggerSystemCommandExecutor implements SystemCommandExecutor {

    @Override
    public void exec(CommandLine command) {

        log.info("SYSTEM COMMAND: " + command);
    }

    @Override
    public int exitValue() {

        return 0;
    }

    @Override
    public boolean isFailure() {

        return false;
    }

    @Override
    public String stringOutput() {

        return "Command executed successfully";
    }

}
