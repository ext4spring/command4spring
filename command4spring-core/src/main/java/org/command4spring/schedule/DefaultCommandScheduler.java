package org.command4spring.schedule;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.result.Result;
import org.command4spring.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.TaskScheduler;

/**
 * Create an instance of this in your spring configuration and set which DispatcherService you want to use to execute
 * scheduled commands. This class automatically looks for ScheduledCommand implementations (through autowire) and
 * execute them.
 * 
 * @author pborbas
 */
public class DefaultCommandScheduler implements CommandScheduler {

    private static final Log LOG = LogFactory.getLog(DefaultCommandScheduler.class);

    private DispatcherService dispatcherService;
    private List<ScheduledCommand> scheduledCommands;
    private TaskScheduler taskScheduler;
    private boolean autostart;

    private class ScheduledTask implements Runnable {

        private DispatcherService dispatcherService;
        private ScheduledCommand<?, Result> scheduledCommand;

        protected ScheduledTask(DispatcherService dispatcherService, ScheduledCommand<?, Result> scheduledCommand) {
            this.dispatcherService = dispatcherService;
            this.scheduledCommand = scheduledCommand;
        }

        @Override
        public void run() {
            try {
                Result result = this.dispatcherService.dispatch(this.scheduledCommand.createCommand());
                this.scheduledCommand.handleResult(result);
            } catch (Throwable e) {
                this.scheduledCommand.handleException(e);
            }
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void schedule() {
        for (ScheduledCommand scheduledCommand : this.scheduledCommands) {
            ScheduledTask task = new ScheduledTask(this.dispatcherService, scheduledCommand);
            this.taskScheduler.schedule(task, scheduledCommand.createTrigger());
            ScheduledFuture future = this.taskScheduler.schedule(task, scheduledCommand.createTrigger());
            LOG.info("Scheduled: " + scheduledCommand.getClass().getName() + ". First run in: " + future.getDelay(TimeUnit.SECONDS) + " seconds");
        }
    }

    @PostConstruct
    public void autostart() {
        if (this.autostart) {
            this.schedule();
        }
    }

    public void setAutostart(boolean autostart) {
        this.autostart = autostart;
    }

    @Required
    public void setDispatcherService(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Required
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Autowired
    public void setScheduledCommands(List<ScheduledCommand> scheduledCommands) {
        this.scheduledCommands = scheduledCommands;
    }

}
