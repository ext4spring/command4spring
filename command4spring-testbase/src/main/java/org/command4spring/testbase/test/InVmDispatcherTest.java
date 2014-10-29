package org.command4spring.testbase.test;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.testbase.dispatcher.TestDispatcherFactory;

public class InVmDispatcherTest extends AbstractDispatcherTest {

    private final InVmDispatcher dispatcher=TestDispatcherFactory.createTestInVmDispatcher();

    @Override
    protected ChainableDispatcher getDispatcher() {
        return this.dispatcher;
    }
}
