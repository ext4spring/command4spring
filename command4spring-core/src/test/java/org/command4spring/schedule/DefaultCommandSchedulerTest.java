package org.command4spring.schedule;

import org.command4spring.dispatcher.Dispatcher;
import org.mockito.Mockito;

public class DefaultCommandSchedulerTest {

    public void testAllScheduledBasedOnScheduledCommads() {
        DefaultCommandScheduler commandScheduler = new DefaultCommandScheduler();
        commandScheduler.setDispatcher(Mockito.mock(Dispatcher.class));
        ScheduledCommand scheduledCommand = Mockito.mock(ScheduledCommand.class);
        //TODO write test
    }

    public void testStartsAutomatically() {
        //TODO write test
    }
}
