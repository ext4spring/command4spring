package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.exception.DispatchException;

/**
 * Filter interface for dispatchers.
 * @author pborbas
 *
 */
public interface CommandFilter {
    /**
     * Filters a Command. It throws an exception in case of error. The previous filter can handle the error or delegate it.
     * call filterChain.filter to forward the command to the next filter
     * @param command
     * @return
     * @throws DispatchException
     */
    public DispatchCommand filter(DispatchCommand dispatchCommand, CommandFilterChain filterChain) throws DispatchException;

}
