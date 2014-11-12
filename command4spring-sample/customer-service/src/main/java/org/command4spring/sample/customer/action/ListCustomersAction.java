package org.command4spring.sample.customer.action;

import org.command4spring.action.AbstractAction;
import org.command4spring.exception.DispatchException;
import org.command4spring.sample.customer.api.ListCustomersCommand;
import org.command4spring.sample.customer.api.ListCustomersResult;
import org.command4spring.sample.customer.repo.CustomerRepository;
import org.command4spring.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

@DispatchAction
public class ListCustomersAction extends AbstractAction<ListCustomersCommand, ListCustomersResult>{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public ListCustomersResult execute(final ListCustomersCommand listCustomersCommand) throws DispatchException {
        return new ListCustomersResult(listCustomersCommand.getCommandId(), this.customerRepository.findAll());
    }

}
