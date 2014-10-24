package org.command4spring.remote.http.receiver;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.dispatcher.callback.DispatcherCallback;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.http.dispatcher.HttpHeader;
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
            this.writeError(e, (HttpServletResponse) this.asyncContext.getResponse());
            this.asyncContext.complete();
        }
    }

    @Override
    public <C extends Command<R>, R extends Result> void beforeDispatch(final C command) throws DispatchException {
    }

    @Override
    public <C extends Command<R>, R extends Result> void onError(final C command, final DispatchException e) throws DispatchException {
        LOGGER.error(e.getMessage(), e);
        try {
            this.writeError(e, (HttpServletResponse) this.asyncContext.getResponse());
        } finally {
            this.asyncContext.complete();
            LOGGER.debug("async process finished");
        }
    }

    @Override
    public <C extends Command<R>, R extends Result> void onSuccess(final C command, final R result) throws DispatchException {
        try {
            LOGGER.debug("command finished, converting to text response");
            this.writeResponse(result, (HttpServletResponse) this.asyncContext.getResponse());
        } finally {
            this.asyncContext.complete();
            LOGGER.debug("async process finished");
        }
    }

    protected void writeErrorResponse(final ServletResponse response, final Throwable t) {
        try {
            // TODO: proper error response
            IOUtils.write(t.getMessage(), this.asyncContext.getResponse().getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Error while writing error to response:" + t.getMessage(), t);
        }
    }

    public void writeResponse(final Result result, final HttpServletResponse httpResponse) throws DispatchException {
        try {
            String textResult = this.serializer.toText(result);
            httpResponse.setHeader(HttpHeader.RESULT_CLASS_HEADER, result.getClass().getName());
            httpResponse.setHeader(HttpHeader.COMMAND_ID_HEADER, result.getCommandId());
            IOUtils.write(textResult, httpResponse.getOutputStream());
        } catch (IOException e) {
            this.writeError(e, httpResponse);
        }
    }

    public void writeError(final Throwable throwable, final HttpServletResponse httpResponse) {
        try {
            httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            httpResponse.setHeader(HttpHeader.RESULT_EXCEPTION_CLASS, throwable.getClass().getName());
            IOUtils.write(throwable.getMessage(), httpResponse.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Error while writing error to response:" + throwable.getMessage(), throwable);
        }
    }

}
