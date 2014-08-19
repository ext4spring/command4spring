package org.command4spring.sample.service.action;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.VoidResult;
import org.command4spring.sample.common.command.PingCommand;

public class PingAction implements Action<PingCommand, VoidResult> {

    @Override
    public VoidResult execute(PingCommand command) throws DispatchException {
        return new VoidResult(command.getCommandId());
    }

    @Override
    public Action<PingCommand, VoidResult> validate(PingCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public Class<PingCommand> getCommandType() {
        return PingCommand.class;
    }
}
