package org.command4spring.sample.billing.api;

import org.command4spring.command.AbstractCommand;
import org.command4spring.result.NoResult;
import org.command4spring.sample.billing.model.Invoice;

public class CreateInvoiceCommand extends AbstractCommand<NoResult> {

    private final Invoice invoice;

    public CreateInvoiceCommand(final Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }
}
