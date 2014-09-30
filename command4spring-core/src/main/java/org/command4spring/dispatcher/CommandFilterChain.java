package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

public interface CommandFilterChain {
    /**
     * Calls the next filter in the chain
     * @param command
     * @return
     * @throws DispatchException
     */
    public <C extends Command<R>, R extends Result> C filter(C command) throws DispatchException;

}
