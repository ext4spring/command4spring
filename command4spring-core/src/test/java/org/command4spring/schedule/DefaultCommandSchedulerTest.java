package org.command4spring.schedule;

import org.command4spring.service.DispatcherService;
import org.mockito.Mockito;

public class DefaultCommandSchedulerTest {

    public void testAllScheduledBasedOnScheduledCommads() {
        DefaultCommandScheduler commandScheduler = new DefaultCommandScheduler();
        commandScheduler.setDispatcherService(Mockito.mock(DispatcherService.class));
        ScheduledCommand scheduledCommand = Mockito.mock(ScheduledCommand.class);
        //TODO write test
    }

    public void testStartsAutomatically() {
        //TODO write test
    }
}
