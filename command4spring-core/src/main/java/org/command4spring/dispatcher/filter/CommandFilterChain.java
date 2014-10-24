package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.exception.DispatchException;

public interface CommandFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
    public DispatchCommand filter(DispatchCommand dispatchCommand) throws DispatchException;

}
