package org.iata.bsplink.sftpaccountmanager.system.command;

import java.io.ByteArrayOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Profile({"!test"})
public class DefaultSystemCommandExecutor implements SystemCommandExecutor {

    private DefaultExecutor executor;
    private ByteArrayOutputStream output;
    private Integer exitValue;

    /**
     * Creates the command executor.
     */
    public DefaultSystemCommandExecutor() {

        output = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(output);

        executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
    }

    @Override
    public void exec(CommandLine command) throws SystemCommandExecutorException {

        try {

            exitValue = executor.execute(command);

        } catch (ExecuteException executeException) {

            throw new SystemCommandExecutorException(command.toString(),
                    executeException.getExitValue(), output.toString(), executeException);

        } catch (Exception exception) {

            throw new SystemCommandExecutorException(command.toString(),exception);
        }
    }

    @Override
    public int exitValue() {

        throwExceptionOnIllegalState();

        return exitValue;
    }

    @Override
    public String stringOutput() {

        throwExceptionOnIllegalState();

        return output.toString();
    }

    private void throwExceptionOnIllegalState() {

        if (exitValue == null) {

            throw new IllegalStateException("No command has been executed");
        }
    }

}
