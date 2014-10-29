package org.command4spring.testbase.action;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.VoidResult;
import org.command4spring.testbase.command.TestWaitCommand;

public class TestWaitAction implements Action<TestWaitCommand, VoidResult>{

    @Override
    public Action<TestWaitCommand, VoidResult> validate(final TestWaitCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public VoidResult execute(final TestWaitCommand command) throws DispatchException {
        try {
            Thread.sleep(command.getWaitTime());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new VoidResult(command.getCommandId());
    }

    @Override
    public Class<TestWaitCommand> getCommandType() {
        return TestWaitCommand.class;
    }

}
