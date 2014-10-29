package org.command4spring.testbase.action;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.NoResult;
import org.command4spring.testbase.command.NoResultCommand;

public class NoResultAction implements Action<NoResultCommand, NoResult>{

    @Override
    public Action<NoResultCommand, NoResult> validate(final NoResultCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public NoResult execute(final NoResultCommand command) throws DispatchException {
        return new NoResult(command.getCommandId());
    }

    @Override
    public Class<NoResultCommand> getCommandType() {
        return NoResultCommand.class;
    }

}