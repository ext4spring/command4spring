package org.command4spring.sample.customer.controller;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.LongResult;
import org.command4spring.sample.customer.api.CreateCustomerCommand;
import org.command4spring.sample.customer.api.ListCustomersCommand;
import org.command4spring.sample.customer.api.ListCustomersResult;
import org.command4spring.sample.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private Dispatcher dispatcher;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() throws DispatchException {
        return "Hello";
    }


    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ListCustomersResult listCustomers() throws DispatchException {
        return this.dispatcher.dispatch(new ListCustomersCommand()).getResult();
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET) 
    public LongResult createCustomer(@PathVariable final long id, @RequestParam final String name) throws DispatchException {
        Customer customer=new Customer(id, name);
        return this.dispatcher.dispatch(new CreateCustomerCommand(customer)).getResult();
    }


}
