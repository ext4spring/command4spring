package org.command4spring.dispatcher;

import java.util.Iterator;
import java.util.List;

import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

public class DefaultResultFilterChain implements ResultFilterChain {

    private final Iterator<ResultFilter> filters;

    public DefaultResultFilterChain(final List<ResultFilter> filters) {
        this.filters = filters.iterator();
    }

    @Override
    public <R extends Result> R filter(final R result) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(result, this);
        }
        return result;
    }

}
