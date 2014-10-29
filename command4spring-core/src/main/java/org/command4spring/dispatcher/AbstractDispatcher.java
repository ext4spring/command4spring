package org.command4spring.dispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.callback.DispatcherCallback;
import org.command4spring.dispatcher.callback.NopDispatcherCallback;
import org.command4spring.dispatcher.filter.DefaultDispatchFilterChain;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.dispatcher.filter.Executor;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;

/**
 * Common logic for all dispatcher implementations
 */
public abstract class AbstractDispatcher implements Dispatcher, ChainableDispatcher {

    private static final Log LOGGER = LogFactory.getLog(AbstractDispatcher.class);

    private ExecutorService executorService;
    private final DispatcherCallback defaultCallback = new NopDispatcherCallback();
    private final List<DispatchFilter> filters;
    private Long timeout;

    public AbstractDispatcher() {
        this(new LinkedList<DispatchFilter>());
    }

    public AbstractDispatcher(final List<DispatchFilter> filters) {
        super();
        this.filters = new LinkedList<DispatchFilter>();
        this.filters.addAll(filters);
        this.executorService = new ForkJoinPool();
        this.timeout = DEFAULT_TIMEOUT;
    }

    /**
     * The returned executor will be added to the end of the filter chain.
     * 
     * @return
     */
    protected abstract Executor getExecutor();

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) throws DispatchException {
        return this.dispatch(command, this.defaultCallback);
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command, final DispatcherCallback callback) throws DispatchException {
        DispatchCommand dispatchCommand = new DispatchCommand(command);
        return this.dispatch(dispatchCommand, callback);
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final DispatchCommand dispatchCommand) throws DispatchException {
        return this.dispatch(dispatchCommand, this.defaultCallback);
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final DispatchCommand dispatchCommand, final DispatcherCallback callback) throws DispatchException {
        Future<DispatchResult<R>> future = this.executorService.submit(new Callable<DispatchResult<R>>() {
            @Override
            public DispatchResult<R> call() throws Exception {
                return AbstractDispatcher.this.executeCommonWorkflow(dispatchCommand, callback);
            }
        });
        return new ResultFuture<R>(future, this.getResultTimeout(dispatchCommand));
    }

    protected long getResultTimeout(final DispatchCommand dispatchCommand) {
        String timeoutHeader = dispatchCommand.getHeader(HEADER_TIMEOUT);
        Long receivedTimeout = (timeoutHeader != null) ? Long.valueOf(timeoutHeader) : null;
        if (receivedTimeout!=null && receivedTimeout<this.timeout) {
            return receivedTimeout;
        }
        return this.timeout;
    }

    @SuppressWarnings("unchecked")
    protected <C extends Command<R>, R extends Result> DispatchResult<R> executeCommonWorkflow(final DispatchCommand dispatchCommand, final DispatcherCallback callback) throws DispatchException {
        LOGGER.debug("Execting command:" + dispatchCommand.getCommand().getCommandType() + ". ID:" + dispatchCommand.getCommand().getCommandId());
        long start = System.currentTimeMillis();
        R result;
        try {
            callback.beforeDispatch(dispatchCommand.getCommand());
            this.addDefaultHeaders(dispatchCommand);
            DispatchResult<R> dispatchResult = (DispatchResult<R>) new DefaultDispatchFilterChain(this.filters, this.getExecutor()).filter(dispatchCommand);
            result = dispatchResult.getResult();
            result.setCommandId(dispatchCommand.getCommand().getCommandId());
            String executionTime = new Long(System.currentTimeMillis() - start).toString();
            this.addDefaultHeaders(dispatchResult, executionTime);
            callback.onSuccess((C) dispatchCommand.getCommand(), result);
            LOGGER.debug("Finished command:" + dispatchCommand.getCommand().getCommandType() + ". ID:" + dispatchCommand.getCommand().getCommandId() + " (" + executionTime + "msec)");
            return dispatchResult;
        } catch (DispatchException e) {
            callback.onError(dispatchCommand.getCommand(), e);
            throw e;
        } catch (Throwable e) {
            DispatchException dispatchException = new DispatchException("Unknown error while executing command:" + e, e);
            callback.onError(dispatchCommand.getCommand(), dispatchException);
            throw dispatchException;
        }
    }

    protected void addDefaultHeaders(final DispatchCommand dispatchCommand) {
        dispatchCommand.setHeader(HEADER_COMMAND_CLASS, dispatchCommand.getCommand().getCommandType().getName());
        dispatchCommand.setHeader(HEADER_COMMAND_ID, dispatchCommand.getCommand().getCommandId());
        dispatchCommand.setHeader(HEADER_TIMEOUT, this.timeout.toString());
    }

    protected void addDefaultHeaders(final DispatchResult<? extends Result> dispatchResult, final String executionTime) {
        dispatchResult.setHeader(HEADER_RESULT_CLASS, dispatchResult.getResult().getClass().getName());
        dispatchResult.setHeader(HEADER_COMMAND_ID, dispatchResult.getResult().getCommandId());
        dispatchResult.setHeader(HEADER_COMMAND_EXECUTION_TIME, executionTime);
    }

    public List<DispatchFilter> getFilters() {
        return this.filters;
    }

    public void addFilter(final DispatchFilter filter) {
        this.getFilters().add(filter);
    }

    public void setExecutorService(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

}
