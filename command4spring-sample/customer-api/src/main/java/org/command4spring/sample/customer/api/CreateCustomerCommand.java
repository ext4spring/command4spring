package org.command4spring.sample.customer.api;

import org.command4spring.command.AbstractCommand;
import org.command4spring.result.LongResult;
import org.command4spring.sample.customer.model.Customer;

public class CreateCustomerCommand extends AbstractCommand<LongResult>{

    private final Customer customer;

    public CreateCustomerCommand(final Customer customer) {
        this.customer=customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
