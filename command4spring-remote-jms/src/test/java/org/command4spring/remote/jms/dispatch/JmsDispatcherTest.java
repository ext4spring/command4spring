package org.command4spring.remote.jms.dispatch;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.testbase.test.AbstractDispatcherTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-jms-test.xml" })
public class JmsDispatcherTest extends AbstractDispatcherTest {

    @Autowired
    JmsDispatcher dispatcher;

    @Override
    protected ChainableDispatcher getDispatcher() {
        return this.dispatcher;
    }
}
