package org.command4spring.sample.customer.api;

import java.util.List;

import org.command4spring.result.AbstractResult;
import org.command4spring.sample.customer.model.Customer;

public class ListCustomersResult extends AbstractResult {

    private final List<Customer> customers;

    public ListCustomersResult(final String commandId, final List<Customer> customers) {
        super(commandId);
        this.customers=customers;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

}
