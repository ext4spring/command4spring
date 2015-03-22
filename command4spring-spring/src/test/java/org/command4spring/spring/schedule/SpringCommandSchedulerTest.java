package org.command4spring.spring.schedule;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.result.Result;
import org.mockito.Mockito;

public class SpringCommandSchedulerTest {

    @SuppressWarnings("unchecked")
    public void testAllScheduledBasedOnScheduledCommads() {
        SpringCommandScheduler commandScheduler = new SpringCommandScheduler();
        commandScheduler.setDispatcher(Mockito.mock(Dispatcher.class));
	ScheduledCommand<?, Result> scheduledCommand = Mockito
		.mock(ScheduledCommand.class);
        //TODO write test
    }

    public void testStartsAutomatically() {
        //TODO write test
    }
}
