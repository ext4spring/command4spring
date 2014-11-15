package org.ext4spring.sample.customer;

import org.command4spring.exception.DispatchException;
import org.command4spring.remote.http.dispatcher.RestHttpDispatcher;
import org.command4spring.sample.customer.CustomerApplication;
import org.command4spring.sample.customer.api.CreateCustomerCommand;
import org.command4spring.sample.customer.api.ListCustomersCommand;
import org.command4spring.sample.customer.model.Customer;
import org.command4spring.serializer.Serializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CustomerApplication.class)
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
public class CustomerApplicationTests {

    @Autowired
    Serializer serializer;
    RestHttpDispatcher dispatcher;

    @Before
    public void initDispatcher() {
	this.dispatcher = new RestHttpDispatcher("http://localhost:8881/dispatcher", serializer);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateCustomer() throws DispatchException {
	dispatcher.dispatch(new CreateCustomerCommand(new Customer(1, "test1"))).getResult();
	Assert.assertEquals(1, dispatcher.dispatch(new ListCustomersCommand()).getResult().getCustomers().size());
    }

}
