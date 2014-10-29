package org.command4spring.testbase.dispatcher;

import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.example.SampleAction;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.testbase.action.NoResultAction;
import org.command4spring.testbase.action.TestFailAction;
import org.command4spring.testbase.action.TestWaitAction;
import org.command4spring.testbase.filter.TestFilter;

public class TestDispatcherFactory {

    public static InVmDispatcher createTestInVmDispatcher() {
        try {
            InVmDispatcher dispatcher = new InVmDispatcher();
            dispatcher.registerAction(new SampleAction());
            dispatcher.registerAction(new TestWaitAction());
            dispatcher.registerAction(new TestFailAction());
            dispatcher.registerAction(new  NoResultAction());
            dispatcher.addFilter(new TestFilter());
            dispatcher.setTimeout(1000);
            return dispatcher;
        } catch (DuplicateActionException e) {
            throw new RuntimeException(e);
        }
    }

}
