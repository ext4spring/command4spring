package org.command4spring.dispatcher;

import java.util.Iterator;
import java.util.List;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

public class DefaultCommandFilterChain implements CommandFilterChain {

    private final Iterator<CommandFilter> filters;

    public DefaultCommandFilterChain(final List<CommandFilter> filters) {
        this.filters = filters.iterator();
    }

    @Override
    public <C extends Command<R>, R extends Result> C filter(final C command) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(command, this);
        }
        return command;
    }

}
