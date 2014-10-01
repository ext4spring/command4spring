package org.command4spring.dispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.action.Action;
import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;

/**
 * Common logic for all dispatcher implementations
 */
public abstract class AbstractDispatcher implements Dispatcher {

    private static final Log LOGGER = LogFactory.getLog(AbstractDispatcher.class);

    private final ExecutorService executorService;
    private List<CommandFilter> commandFilters = new LinkedList<CommandFilter>();
    private List<ResultFilter> resultFilters = new LinkedList<ResultFilter>();

    public AbstractDispatcher() {
        int cores = Runtime.getRuntime().availableProcessors();
        this.executorService=new ForkJoinPool(cores);
    }

    public AbstractDispatcher(final ExecutorService executorService) {
        super();
        this.executorService = executorService;
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) throws DispatchException {
        Future<R> future=this.executorService.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return AbstractDispatcher.this.executeCommonWorkflow(command);
            }
        });
        return new ResultFuture<R>(future);
    }

    protected <C extends Command<R>, R extends Result> R executeCommonWorkflow(final C command) throws DispatchException {
        LOGGER.debug("Execting command:" + command);
        long start = System.currentTimeMillis();
        R result = this.filterResult(this.execute(this.filterCommand(command)));
        result.setCommandId(command.getCommandId());
        LOGGER.debug("Finished command:" + command + " (" + (System.currentTimeMillis() - start) + "msec)");
        return result;
    }


    protected <C extends Command<R>, R extends Result> C filterCommand(final C command) throws DispatchException {
        CommandFilterChain filterChain = new DefaultCommandFilterChain(this.commandFilters);
        return filterChain.filter(command);
    }

    protected <R extends Result> R filterResult(final R result) throws DispatchException {
        ResultFilterChain filterChain = new DefaultResultFilterChain(this.resultFilters);
        return filterChain.filter(result);
    }

    public void setCommandFilters(final List<CommandFilter> commandFilters) {
        this.commandFilters = commandFilters;
    }

    public void setResultFilters(final List<ResultFilter> resultFilters) {
        this.resultFilters = resultFilters;
    }

    public List<CommandFilter> getCommandFilters() {
        return this.commandFilters;
    }

    public List<ResultFilter> getResultFilters() {
        return this.resultFilters;
    }

    /**
     * Delegate work to the {@link Action}
     * 
     * @param command
     * @return
     * @throws DispatchException
     */
    protected abstract <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException;

}
