package org.command4spring.remote.http.receiver;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.dispatcher.DispatcherCallback;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

public class AsyncServletProcess implements DispatcherCallback {
    private static final Log LOGGER = LogFactory.getLog(AsyncServletProcess.class);

    private final Serializer serializer;
    private final Dispatcher dispatcher;
    private final Command<? extends Result> command;
    private final AsyncContext asyncContext;

    public AsyncServletProcess(final AsyncContext asyncContext, final Command<? extends Result> command, final Serializer serializer, final Dispatcher dispatcher) {
	this.serializer = serializer;
	this.dispatcher = dispatcher;
	this.command = command;
	this.asyncContext = asyncContext;
    }

    /**
     * Executes the request with the {@link Dispatcher} and registers itself as
     * a {@link DispatcherCallback} to write the result as text into http
     * response and close the async context
     */
    protected void execute() {
	try {
	    LOGGER.debug("delegating command to dispatcher");
	    this.dispatcher.dispatch(this.command, this);
	} catch (DispatchException e) {
	    LOGGER.error("Error while processing request:" + e.getMessage(), e);
	    writeErrorResponse(asyncContext.getResponse(), e);
	    this.asyncContext.complete();
	}
    }

    @Override
    public <C extends Command<R>, R extends Result> void beforeDispatch(C command) throws DispatchException {
    }

    @Override
    public <C extends Command<R>, R extends Result> void onError(C command, DispatchException e) throws DispatchException {
	LOGGER.error(e.getMessage(), e);
	writeErrorResponse(asyncContext.getResponse(), e);
	this.asyncContext.complete();
	LOGGER.debug("async process finished");
    }

    @Override
    public <C extends Command<R>, R extends Result> void onSuccess(C command, R result) throws DispatchException {
	try {
	    LOGGER.debug("command finished, converting to text response");
	    String textResult = serializer.toText(result);
	    IOUtils.write(textResult, asyncContext.getResponse().getOutputStream());
	} catch (IOException e) {
	    this.onError(command, new DispatchException("Error while writing response to HTTP output stream:" + e, e));
	} finally {
	    asyncContext.complete();
	    LOGGER.debug("async process finished");
	}
    }

    protected void writeErrorResponse(ServletResponse response, Throwable t) {
	try {
	    // TODO: proper error response
	    IOUtils.write(t.getMessage(), asyncContext.getResponse().getOutputStream());
	} catch (IOException e) {
	    LOGGER.error("Error while writing error to response:" + t.getMessage(), t);
	}
    }

}
