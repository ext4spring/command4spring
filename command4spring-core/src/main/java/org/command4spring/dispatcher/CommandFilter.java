package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

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
    public <C extends Command<R>, R extends Result> C filter(C command, CommandFilterChain filterChain) throws DispatchException;

}
