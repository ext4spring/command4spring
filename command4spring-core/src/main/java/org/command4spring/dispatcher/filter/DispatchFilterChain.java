package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

public interface DispatchFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
    public DispatchResult filter(DispatchCommand dispatchCommand) throws DispatchException;

}
