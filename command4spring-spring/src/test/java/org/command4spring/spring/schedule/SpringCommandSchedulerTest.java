package org.command4spring.spring.schedule;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.spring.schedule.ScheduledCommand;
import org.command4spring.spring.schedule.SpringCommandScheduler;
import org.mockito.Mockito;

public class SpringCommandSchedulerTest {

    public void testAllScheduledBasedOnScheduledCommads() {
        SpringCommandScheduler commandScheduler = new SpringCommandScheduler();
        commandScheduler.setDispatcher(Mockito.mock(Dispatcher.class));
        ScheduledCommand scheduledCommand = Mockito.mock(ScheduledCommand.class);
        //TODO write test
    }

    public void testStartsAutomatically() {
        //TODO write test
    }
}
