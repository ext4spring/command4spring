package org.command4spring.remote.receiver;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.model.CommandMessage;
import org.command4spring.remote.model.ResultMessage;
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
    public ResultMessage execute(final CommandMessage commandMessage) throws DispatchException {
        Command<? extends Result> command = null;
        try {
            command = this.serializer.toCommand(commandMessage.getTextCommand());
            Result result = this.dispatcher.dispatch(command).getResult(this.timeout, TimeUnit.MILLISECONDS);
            return this.createResultMessage(result);
        } catch (Throwable e) {
            LOGGER.error("Error while dispatching command:" + e, e);
            return this.createErrorResultMessage(command, e);
        }
    }

    protected ResultMessage createResultMessage(final Result result) throws CommandSerializationException {
        ResultMessage resultMessage = new ResultMessage(this.serializer.toText(result));
        resultMessage.setHeader(CommandMessage.COMMAND_CLASS_HEADER, result.getClass().getName());
        resultMessage.setHeader(CommandMessage.COMMAND_ID_HEADER, result.getCommandId());
        return resultMessage;
    }

    protected ResultMessage createErrorResultMessage(final Command<? extends Result> command, final Throwable t) {
        ResultMessage resultMessage = new ResultMessage(t.getMessage());
        if (command != null) {
            resultMessage.setHeader(CommandMessage.COMMAND_CLASS_HEADER, command.getClass().getName());
            resultMessage.setHeader(CommandMessage.COMMAND_ID_HEADER, command.getCommandId());
        }
        resultMessage.setHeader(ResultMessage.RESULT_EXCEPTION_CLASS, t.getClass().getName());
        return resultMessage;
    }
}
