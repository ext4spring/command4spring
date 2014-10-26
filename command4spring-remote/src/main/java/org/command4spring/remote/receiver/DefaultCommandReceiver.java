package org.command4spring.remote.receiver;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.RemoteDispatcher;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
public class DefaultCommandReceiver implements CommandReceiver {

    private static Log LOGGER = LogFactory.getLog(DefaultCommandReceiver.class);

    private final Serializer serializer;
    private final ChainableDispatcher dispatcher;
    private final long timeout = 5000;

    public DefaultCommandReceiver(final Serializer serializer, final ChainableDispatcher dispatcher) {
        super();
        this.serializer = serializer;
        this.dispatcher = dispatcher;
    }

    @Override
    public TextDispatcherResult execute(final TextDispatcherCommand textDispatcherCommand) throws DispatchException {
	DispatchCommand dispatchCommand = null;
        try {
	    dispatchCommand = this.convertRequest(textDispatcherCommand);
	    DispatchResult<? extends Result> dispatchResult = this.dispatcher.dispatch(dispatchCommand).getDispatchResult(this.timeout, TimeUnit.MILLISECONDS);
	    return this.convertResult(dispatchResult);
        } catch (Throwable e) {
            LOGGER.error("Error while dispatching command: " + e, e);
	    return this.createErrorResultMessage(dispatchCommand, e);
        }
    }

    protected DispatchCommand convertRequest(TextDispatcherCommand textDispatcherCommand) throws CommandSerializationException {
	DispatchCommand dispatchCommand = new DispatchCommand(this.serializer.toCommand(textDispatcherCommand.getTextCommand()));
	dispatchCommand.getHeaders().putAll(textDispatcherCommand.getHeaders());
	return dispatchCommand;
    }

    protected TextDispatcherResult convertResult(final DispatchResult<? extends Result> dispatchResult) throws CommandSerializationException {
	TextDispatcherResult resultMessage = new TextDispatcherResult(dispatchResult.getResult().getCommandId(), this.serializer.toText(dispatchResult.getResult()));
	resultMessage.getHeaders().putAll(dispatchResult.getHeaders());
        return resultMessage;
    }

    protected TextDispatcherResult createErrorResultMessage(final DispatchCommand dispatchCommand, final Throwable t) {
	String commandId = (dispatchCommand == null) ? null : dispatchCommand.getCommand().getCommandId();
	TextDispatcherResult resultMessage = new TextDispatcherResult(commandId, t.getMessage());
	resultMessage.setHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS, t.getClass().getName());
        return resultMessage;
    }
}
