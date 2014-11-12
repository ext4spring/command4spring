package org.command4spring.sample.customer.action;

import org.command4spring.action.AbstractAction;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.LongResult;
import org.command4spring.sample.customer.api.CreateCustomerCommand;
import org.command4spring.sample.customer.repo.CustomerRepository;
import org.command4spring.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

@DispatchAction
public class CreateCustomerAction extends AbstractAction<CreateCustomerCommand, LongResult>{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public LongResult execute(final CreateCustomerCommand createCustomerCommand) throws DispatchException {
        long customerId=this.customerRepository.save(createCustomerCommand.getCustomer()).getId();
        return new LongResult(createCustomerCommand.getCommandId(), customerId);
    }
}
