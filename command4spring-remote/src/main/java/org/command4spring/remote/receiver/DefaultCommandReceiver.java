package org.command4spring.remote.receiver;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.RemoteDispatcher;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
public class DefaultCommandReceiver implements CommandReceiver {

    private static Log LOGGER = LogFactory.getLog(DefaultCommandReceiver.class);

    private final Serializer serializer;
    private final Dispatcher dispatcher;
    private final long timeout = 5000;

    public DefaultCommandReceiver(final Serializer serializer, final Dispatcher dispatcher) {
        super();
        this.serializer = serializer;
        this.dispatcher = dispatcher;
    }

    @Override
    public TextDispatcherResult execute(final TextDispatcherCommand commandMessage) throws DispatchException {
        Command<? extends Result> command = null;
        try {
            command = this.serializer.toCommand(commandMessage.getTextCommand());
            Result result = this.dispatcher.dispatch(command).getResult(this.timeout, TimeUnit.MILLISECONDS);
            return this.createResultMessage(result);
        } catch (Throwable e) {
            LOGGER.error("Error while dispatching command: " + e, e);
            return this.createErrorResultMessage(command, e);
        }
    }

    protected TextDispatcherResult createResultMessage(final Result result) throws CommandSerializationException {
        TextDispatcherResult resultMessage = new TextDispatcherResult(result.getCommandId(), this.serializer.toText(result));
        return resultMessage;
    }

    protected TextDispatcherResult createErrorResultMessage(final Command<? extends Result> command, final Throwable t) {
	String commandId = (command == null) ? null : command.getCommandId();
	TextDispatcherResult resultMessage = new TextDispatcherResult(commandId, t.getMessage());
	resultMessage.setHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS, t.getClass().getName());
        return resultMessage;
    }
}
