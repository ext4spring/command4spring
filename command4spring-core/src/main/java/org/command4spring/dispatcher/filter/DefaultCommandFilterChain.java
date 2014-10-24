package org.command4spring.dispatcher.filter;

import java.util.Iterator;
import java.util.List;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.exception.DispatchException;

public class DefaultCommandFilterChain implements CommandFilterChain {

    private final Iterator<CommandFilter> filters;

    public DefaultCommandFilterChain(final List<CommandFilter> filters) {
        this.filters = filters.iterator();
    }

    @Override
    public DispatchCommand filter(DispatchCommand dispatchCommand) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(dispatchCommand, this);
        }
        return dispatchCommand;
    }
    

}
