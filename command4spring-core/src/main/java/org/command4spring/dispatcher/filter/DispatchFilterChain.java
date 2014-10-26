package org.command4spring.dispatcher.filter;

import org.command4spring.command.DispatchCommand;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;

public interface DispatchFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
    public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand) throws DispatchException;

}
