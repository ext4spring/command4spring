package org.command4spring.remote.jms.dispatch;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.VoidResult;

public class TestFailAction implements Action<TestFailCommand, VoidResult>{

    @Override
    public Action<TestFailCommand, VoidResult> validate(final TestFailCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public VoidResult execute(final TestFailCommand command) throws DispatchException {
        throw new TestDispatchException("This will always fail:(");
    }

    @Override
    public Class<TestFailCommand> getCommandType() {
        return TestFailCommand.class;
    }

}
