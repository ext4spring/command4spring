package org.command4spring.sample.customer.api;

import org.command4spring.result.AbstractResult;

public class ListCustomersResult extends AbstractResult {

    public ListCustomersResult(final String commandId) {
        super(commandId);
    }

}
