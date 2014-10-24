package org.command4spring.remote.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.dispatcher.filter.Executor;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.ExceptionUtil;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
import org.command4spring.util.CommandUtil;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 */
public abstract class AbstractRemoteDispatcher extends AbstractDispatcher implements RemoteDispatcher, Executor {

    private static final Log LOGGER = LogFactory.getLog(AbstractRemoteDispatcher.class);

    private final Serializer serializer;

    public AbstractRemoteDispatcher(final Serializer serializer) {
	super();
	this.serializer = serializer;
    }

    @Override
    protected Executor getExecutor() {
	return this;
    }

    @Override
    public DispatchResult execute(DispatchCommand dispatchCommand) throws DispatchException {
	LOGGER.debug("Executing remote command");
	TextDispatcherCommand textDispatcherCommand=this.serializeCommand(dispatchCommand);
	TextDispatcherResult textDispatcherResult=this.executeRemote(dispatchCommand.getCommand(), textDispatcherCommand);
	return this.parseResult(dispatchCommand.getCommand(), textDispatcherResult);
    }

    protected DispatchResult parseResult(Command<? extends Result> command, TextDispatcherResult textDispatcherResult) throws DispatchException {
	LOGGER.debug("Parsing result after remote execution");
	if (textDispatcherResult.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS) != null) {
	    LOGGER.error("Result contains exception headers");
	    throw ExceptionUtil.instantiateDispatchException(textDispatcherResult.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS), textDispatcherResult.getTextResult());
	}
	DispatchResult dispatchResult;
	if (CommandUtil.isNoResultCommand(command)) {
	    return dispatchResult=new DispatchResult(new NoResult(command.getCommandId()));
	} else {
	    dispatchResult=new DispatchResult(this.serializer.toResult(textDispatcherResult.getTextResult()));
	}
	dispatchResult.getHeaders().putAll(textDispatcherResult.getHeaders());
	return dispatchResult;
    }

    /**
     * Creates a basic command message which contains all the library specific
     * headers
     * 
     * @param command
     * @return
     * @throws CommandSerializationException
     */
    protected TextDispatcherCommand serializeCommand(DispatchCommand dispatchCommand) throws CommandSerializationException {
	LOGGER.debug("Serializing command before remote execution");
	TextDispatcherCommand textDispatcherCommand=new TextDispatcherCommand(this.serializer.toText(dispatchCommand.getCommand()));
	textDispatcherCommand.getHeaders().putAll(dispatchCommand.getHeaders());
	return textDispatcherCommand;
    }

    /**
     * Converts the message into its protocoll specific representation (text
     * command body and headers) and sends it through the wire. It receives the
     * response, copies the result and headers into the response
     * 
     * @param command
     *            . The original command
     * @param textDispatchCommand
     *            . Prepared command message with headers and serialized text
     *            command
     * @return
     * @throws DispatchException
     */
    protected abstract TextDispatcherResult executeRemote(Command<? extends Result> command, TextDispatcherCommand textDispatchCommand) throws DispatchException;

}
