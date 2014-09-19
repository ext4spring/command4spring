package org.command4spring.sample.service.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.StringResult;
import org.command4spring.sample.common.command.GetTimeCommand;

public class GetTimeAction implements Action<GetTimeCommand, StringResult> {

    private final SimpleDateFormat DF=new SimpleDateFormat("YYYY-MM-DD");
    
    @Override
    public StringResult execute(GetTimeCommand command) throws DispatchException {
        return new StringResult(command.getCommandId(), DF.format(new Date()));
    }

    @Override
    public Action<GetTimeCommand, StringResult> validate(GetTimeCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public Class<GetTimeCommand> getCommandType() {
        return GetTimeCommand.class;
    }
}
