package org.command4spring.testbase.test;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.dispatcher.filter.retry.DefaultRetryPolicy;
import org.command4spring.dispatcher.filter.retry.RetryFilter;
import org.command4spring.exception.DispatchException;
import org.command4spring.testbase.command.TestFailCommand;
import org.command4spring.testbase.dispatcher.TestDispatcherFactory;
import org.command4spring.testbase.exception.TestDispatchException;
import org.junit.Test;

public class InVmDispatcherTest extends AbstractDispatcherTest {

    private final InVmDispatcher dispatcher=TestDispatcherFactory.createTestInVmDispatcher();

    @Override
    protected ChainableDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Test(expected=TestDispatchException.class)
    public void testRetry() throws DispatchException {
        InVmDispatcher inVmDispatcher=TestDispatcherFactory.createTestInVmDispatcher();
        inVmDispatcher.setTimeout(30000);
        DefaultRetryPolicy retryPolicy=new DefaultRetryPolicy();
        retryPolicy.setBaseWaitTime(100);
        retryPolicy.setMaxRetryCount(3);
        retryPolicy.setMultiplier(2);
        inVmDispatcher.addFilter(new RetryFilter(retryPolicy));
        inVmDispatcher.dispatch(new TestFailCommand()).getResult();

    }

}
