package org.command4spring.example;

import org.command4spring.action.Action;
import org.command4spring.action.DispatchAction;

@DispatchAction
public class SampleAction implements Action<SampleCommand, SampleResult> {

	@Override
	public SampleResult execute(SampleCommand command) {
		return new SampleResult();
	}

	@Override
	public Class<SampleCommand> getCommandType() {
		return SampleCommand.class;
	}
}
