package org.command4spring.dispatcher.filter;

import java.util.Iterator;
import java.util.List;

import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

public class DefaultResultFilterChain implements ResultFilterChain {

    private final Iterator<ResultFilter> filters;

    public DefaultResultFilterChain(final List<ResultFilter> filters) {
	this.filters = filters.iterator();
    }

    @Override
    public DispatchResult filter(DispatchResult dispatchResult) throws DispatchException {
	if (this.filters.hasNext()) {
	    return this.filters.next().filter(dispatchResult, this);
	}
	return dispatchResult;
    }

}
