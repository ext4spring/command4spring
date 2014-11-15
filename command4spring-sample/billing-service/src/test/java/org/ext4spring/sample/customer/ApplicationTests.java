package org.ext4spring.sample.customer;

import org.command4spring.sample.billing.BillingApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BillingApplication.class)
@WebAppConfiguration
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }

}
