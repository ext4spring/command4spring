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

    private static final long DEFAULT_TIMEOUT = 30000;
    private static final Log LOGGER = LogFactory.getLog(AbstractDispatcher.class);

    private ExecutorService executorService;
    private final DispatcherCallback defaultCallback = new NopDispatcherCallback();
    private final List<DispatchFilter> filters;
    private long timeout;

    public AbstractDispatcher() {
	this(new LinkedList<DispatchFilter>());
    }

    public AbstractDispatcher(List<DispatchFilter> filters) {
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
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand) throws DispatchException {
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
	return new ResultFuture<R>(future, this.timeout);
    }

    @SuppressWarnings("unchecked")
    protected <C extends Command<R>, R extends Result> DispatchResult<R> executeCommonWorkflow(final DispatchCommand dispatchCommand, final DispatcherCallback callback) throws DispatchException {
	LOGGER.debug("Execting command:" + dispatchCommand.getCommand().getCommandType() + ". ID:" + dispatchCommand.getCommand().getCommandId());
	long start = System.currentTimeMillis();
	R result;
	try {
	    callback.beforeDispatch(dispatchCommand.getCommand());
	    this.addDefaultHeaders(dispatchCommand);
	    DispatchResult<R> dispatchResult = (DispatchResult<R>) new DefaultDispatchFilterChain(filters, this.getExecutor()).filter(dispatchCommand);
	    result = (R) dispatchResult.getResult();
	    result.setCommandId(dispatchCommand.getCommand().getCommandId());
	    this.addDefaultHeaders(dispatchResult);
	    callback.onSuccess((C) dispatchCommand.getCommand(), result);
	    LOGGER.debug("Finished command:" + dispatchCommand.getCommand().getCommandType() + ". ID:" + dispatchCommand.getCommand().getCommandId() + " (" + (System.currentTimeMillis() - start)
		    + "msec)");
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

    protected void addDefaultHeaders(DispatchCommand dispatchCommand) {
	dispatchCommand.setHeader(Dispatcher.HEADER_COMMAND_CLASS, dispatchCommand.getCommand().getCommandType().getName());
	dispatchCommand.setHeader(Dispatcher.HEADER_COMMAND_ID, dispatchCommand.getCommand().getCommandId());
    }

    protected void addDefaultHeaders(DispatchResult<? extends Result> dispatchResult) {
	dispatchResult.setHeader(Dispatcher.HEADER_RESULT_CLASS, dispatchResult.getResult().getClass().getName());
	dispatchResult.setHeader(Dispatcher.HEADER_COMMAND_ID, dispatchResult.getResult().getCommandId());
    }

    public List<DispatchFilter> getFilters() {
	return filters;
    }

    public void addFilter(DispatchFilter filter) {
	this.getFilters().add(filter);
    }

    public void setExecutorService(ExecutorService executorService) {
	this.executorService = executorService;
    }

    public void setTimeout(long timeout) {
	this.timeout = timeout;
    }

}
