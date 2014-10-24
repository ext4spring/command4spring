package org.command4spring.dispatcher.filter;

import java.util.Iterator;
import java.util.List;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

public class DefaultDispatchFilterChain implements DispatchFilterChain {

    private final Iterator<DispatchFilter> filters;
    private final Executor executor;

    public DefaultDispatchFilterChain(final List<DispatchFilter> filters, Executor executor) {
        this.filters = filters.iterator();
	this.executor = executor;
    }

    @Override
    public DispatchResult filter(DispatchCommand dispatchCommand) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(dispatchCommand, this);
        }
	return executor.execute(dispatchCommand);
    }
    

}
