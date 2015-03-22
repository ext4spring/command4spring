package org.command4spring.remote.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.dispatcher.filter.Executor;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.ExceptionUtil;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.remote.serializer.Serializer;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.util.CommandUtil;

/**
 * Common logic of the {@link RemoteDispatcher}
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
    public DispatchResult<Result> execute(final DispatchCommand dispatchCommand) throws DispatchException {
        LOGGER.debug("Executing remote command");
        TextDispatcherCommand textDispatcherCommand=this.serializeCommand(dispatchCommand);
        TextDispatcherResult textDispatcherResult=this.executeRemote(dispatchCommand.getCommand(), textDispatcherCommand, this.getTimeout());
        return this.parseResult(dispatchCommand.getCommand(), textDispatcherResult);
    }

    protected DispatchResult<Result> parseResult(final Command<? extends Result> command, final TextDispatcherResult textDispatcherResult) throws DispatchException {
        LOGGER.debug("Parsing result after remote execution");
        if (textDispatcherResult.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS) != null) {
            LOGGER.error("Result contains exception headers");
            throw ExceptionUtil.instantiateDispatchException(textDispatcherResult.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS), textDispatcherResult.getTextResult());
        }
        DispatchResult<Result> dispatchResult;
        if (CommandUtil.isNoResultCommand(command)) {
            return dispatchResult=new DispatchResult<Result>(new NoResult(command.getCommandId()));
        } else {
            dispatchResult=new DispatchResult<Result>(this.serializer.toResult(textDispatcherResult.getTextResult()));
        }
        dispatchResult.getHeaders().putAll(textDispatcherResult.getHeaders());
        return dispatchResult;
    }

    /**
     * Creates a basic command message which contains all the library specific
     * headers
     * 
     * @throws CommandSerializationException
     */
    protected TextDispatcherCommand serializeCommand(final DispatchCommand dispatchCommand) throws CommandSerializationException {
        LOGGER.debug("Serializing command before remote execution");
        TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(this.serializer.toText(dispatchCommand.getCommand()));
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
    protected abstract TextDispatcherResult executeRemote(Command<? extends Result> command, TextDispatcherCommand textDispatchCommand, Long timeout) throws DispatchException;

}
