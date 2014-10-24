package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

/**
 * Filter interface for dispatchers.
 * @author pborbas
 *
 */
public interface ResultFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
    public DispatchResult filter(DispatchResult dispatchResult) throws DispatchException;
}
