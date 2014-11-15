package org.ext4spring.sample.customer;

import org.command4spring.exception.DispatchException;
import org.command4spring.remote.jms.dispatch.JmsDispatcher;
import org.command4spring.remote.jms.dispatch.JmsTemplate;
import org.command4spring.sample.billing.BillingApplication;
import org.command4spring.sample.billing.api.CreateInvoiceCommand;
import org.command4spring.sample.billing.model.Invoice;
import org.command4spring.serializer.Serializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BillingApplication.class)
@WebAppConfiguration
public class BillingApplicationTests {

    @Autowired
    Serializer serializer;
    @Autowired
    @Qualifier("commandJmsTemplate")
    JmsTemplate commandJmsTemplate;
    @Autowired
    @Qualifier("resultJmsTemplate")
    JmsTemplate resultJmsTemplate;

    JmsDispatcher dispatcher;

    @Before
    public void initDispatcher() {
	dispatcher = new JmsDispatcher(commandJmsTemplate, resultJmsTemplate, serializer);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void createInvoice() throws DispatchException {
	CreateInvoiceCommand command = new CreateInvoiceCommand(new Invoice(1l, "inv1", 5000));
	this.dispatcher.dispatch(command).getResult();
    }
}
