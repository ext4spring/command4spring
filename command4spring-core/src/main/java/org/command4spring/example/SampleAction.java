package org.command4spring.example;

import org.command4spring.action.Action;
import org.command4spring.action.DispatchAction;
import org.command4spring.exception.CommandValidationException;

@DispatchAction
public class SampleAction implements Action<SampleCommand, SampleResult> {

	@Override
	public SampleResult execute(SampleCommand command) {
		return new SampleResult(command.getCommandId());
	}

	@Override
	public Class<SampleCommand> getCommandType() {
		return SampleCommand.class;
	}
	
	@Override
	public Action<SampleCommand, SampleResult> validate(SampleCommand command) throws CommandValidationException {
	    if (command.getData()!=null && command.getData().length()>5) throw new CommandValidationException(command, "Invalid SampleCommand.", "Data max size is 5 characters");
	    return this;
	}
}
