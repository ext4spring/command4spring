package org.command4spring.remote.jms.dispatch;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.NoResult;

public class TestAction implements Action<TestCommand, NoResult>{

    @Override
    public Action<TestCommand, NoResult> validate(final TestCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public NoResult execute(final TestCommand command) throws DispatchException {
        return new NoResult(command.getCommandId());
    }

    @Override
    public Class<TestCommand> getCommandType() {
        return TestCommand.class;
    }

}
