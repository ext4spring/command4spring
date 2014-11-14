package org.command4spring.sample.billing.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.action.AbstractAction;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.NoResult;
import org.command4spring.sample.billing.api.CreateInvoiceCommand;
import org.command4spring.sample.billing.model.Invoice;
import org.command4spring.sample.billing.repo.InvoiceRepository;
import org.command4spring.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DispatchAction
public class CreateInvoiceAction extends AbstractAction<CreateInvoiceCommand, NoResult>{

    private final Log LOG=LogFactory.getLog(CreateInvoiceAction.class);

    @Autowired
    InvoiceRepository InvoiceRepository;

    @Override
    @Transactional
    public NoResult execute(final CreateInvoiceCommand createInvoiceCommand) throws DispatchException {
        Invoice createdInvoice=this.InvoiceRepository.save(createInvoiceCommand.getInvoice());
        this.LOG.info("invoice created:"+createdInvoice.getId());
        return new NoResult(createInvoiceCommand.getCommandId());
    }

}