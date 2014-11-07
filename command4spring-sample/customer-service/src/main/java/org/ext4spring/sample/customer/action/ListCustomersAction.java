package org.ext4spring.sample.customer.action;

import org.command4spring.action.Action;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.sample.customer.api.ListCustomersCommand;
import org.command4spring.sample.customer.api.ListCustomersResult;

public class ListCustomersAction implements Action<ListCustomersCommand, ListCustomersResult>{

    @Override
    public ListCustomersResult execute(final ListCustomersCommand listCustomersCommand) throws DispatchException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<ListCustomersCommand> getCommandType() {
        return ListCustomersCommand.class;
    }

    @Override
    public Action<ListCustomersCommand, ListCustomersResult> validate(final ListCustomersCommand listCustomersCommand) throws CommandValidationException {
        return this;
    }




}
