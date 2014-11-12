package org.command4spring.sample.billing.controller;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.sample.billing.api.CreateInvoiceCommand;
import org.command4spring.sample.billing.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {

    @Autowired
    private Dispatcher dispatcher;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String dispatcherInfo() throws DispatchException {
        return this.dispatcher.toString().replaceAll("\n", "<BR/>");
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public void createInvoice(@RequestBody final Invoice invoice) throws DispatchException {
        this.dispatcher.dispatch(new CreateInvoiceCommand(invoice));
    }

}
