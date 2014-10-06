package org.command4spring.sample.service.action;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.VoidResult;
import org.command4spring.sample.common.command.GetTimeCommand;
import org.command4spring.sample.common.command.SleepCommand;
import org.command4spring.xml.serializer.XmlSerializer;
import org.springframework.stereotype.Component;

@Component
public class SleepAction implements Action<SleepCommand, VoidResult> {

    @Override
    public VoidResult execute(final SleepCommand command) throws DispatchException {
	if (command.getTime() > 0) {
	    try {
		Thread.sleep(command.getTime());
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	return new VoidResult(command.getCommandId());
    }

    @Override
    public Action<SleepCommand, VoidResult> validate(final SleepCommand command) throws CommandValidationException {
	return this;
    }

    @Override
    public Class<SleepCommand> getCommandType() {
	return SleepCommand.class;
    }

    public static void main(final String[] args) {
	System.out.println(new XmlSerializer().toText(new GetTimeCommand()));
    }
}
