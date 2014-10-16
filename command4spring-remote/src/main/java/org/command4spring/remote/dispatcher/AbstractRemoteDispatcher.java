package org.command4spring.remote.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.ExceptionUtil;
import org.command4spring.remote.model.CommandMessage;
import org.command4spring.remote.model.ResultMessage;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 */
public abstract class AbstractRemoteDispatcher extends AbstractDispatcher implements RemoteDispatcher {

    private static final Log LOGGER = LogFactory.getLog(AbstractRemoteDispatcher.class);

    private final Serializer serializer;

    public AbstractRemoteDispatcher(final Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    @Override
    protected <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException {
        CommandMessage commandMessage = this.createCommandMessage(command);
        ResultMessage resultMessage = this.executeRemote(commandMessage);
        if (resultMessage.getHeader(ResultMessage.RESULT_EXCEPTION_CLASS) != null) {
            throw ExceptionUtil.instantiateDispatchException(resultMessage.getHeader(ResultMessage.RESULT_EXCEPTION_CLASS), resultMessage.getTextResult());
        }
        if (commandMessage.isNoResultCommand()) {
            return (R) new NoResult(command.getCommandId());
        }
        R result = (R) this.serializer.toResult(resultMessage.getTextResult());
        return result;

    }

    /**
     * Creates a basic command message which contains all the library specific headers
     * 
     * @param command
     * @return
     * @throws CommandSerializationException
     */
    protected CommandMessage createCommandMessage(final Command<? extends Result> command) throws CommandSerializationException {
        String textCommand = this.serializer.toText(command);
        CommandMessage commandMessage = new CommandMessage(command, textCommand);
        commandMessage.setHeader(CommandMessage.COMMAND_CLASS_HEADER, command.getClass().getName());
        commandMessage.setHeader(CommandMessage.COMMAND_ID_HEADER, command.getCommandId());
        return commandMessage;
    }

    /**
     * Converts the message into its protocoll specific representation (text command body and headers) and sends it
     * through the wire. It receives the response, copies the result and headers into the response
     * 
     * @param commandMessage
     * @return
     * @throws DispatchException
     */
    protected abstract ResultMessage executeRemote(CommandMessage commandMessage) throws DispatchException;

}
