package org.command4spring.sample.billing.action;

import org.command4spring.action.AbstractAction;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.NoResult;
import org.command4spring.sample.billing.api.CreateInvoiceCommand;
import org.command4spring.sample.billing.repo.InvoiceRepository;
import org.command4spring.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DispatchAction
public class CreateInvoiceAction extends AbstractAction<CreateInvoiceCommand, NoResult>{

    @Autowired
    InvoiceRepository InvoiceRepository;

    @Override
    @Transactional
    public NoResult execute(final CreateInvoiceCommand createInvoiceCommand) throws DispatchException {
        this.InvoiceRepository.save(createInvoiceCommand.getInvoice());
        return new NoResult(createInvoiceCommand.getCommandId());
    }

}
