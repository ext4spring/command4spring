package org.command4spring.example;

import org.command4spring.action.AbstractAction;

public class SampleAction extends AbstractAction<SampleCommand, SampleResult> {

    @Override
    public SampleResult execute(final SampleCommand command) {
        return new SampleResult(command.getCommandId());
    }

}
