package org.iata.bsplink.sftpaccountmanager.system.command;

import lombok.Getter;

@Getter
public class SystemCommandExecutorException extends Exception {

    private static final int UNKNOWN_EXIT_VALUE = -1;
    private static final long serialVersionUID = 1L;

    private final int exitValue;
    private final String command;
    private final String output;
    private final String message;

    public SystemCommandExecutorException(String command, Throwable cause) {

        this(command, UNKNOWN_EXIT_VALUE, null, cause);
    }

    /**
     * Creates a exception with a command output and exit value.
     */
    public SystemCommandExecutorException(String command, int exitValue, String output,
            Throwable cause) {

        super(cause);

        this.command = command;
        this.exitValue = exitValue;
        this.output = output;

        message = String.format("Error executing command: [%s] return value: [%d] output: [%s]",
                command, exitValue, output);

    }

    @Override
    public String getMessage() {

        return message;
    }
}
