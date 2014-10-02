package org.command4spring.remote.http.receiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.command4spring.remote.receiver.CommandReceiver;

public abstract class CommandReceiverListener implements ServletContextListener{

    protected static final String EXECUTOR_ATTRIBUTE="executor";
    public static final String COMMAND_RECEIVER_ATTRIBUTE = "command_receiver";

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        sce.getServletContext().setAttribute(EXECUTOR_ATTRIBUTE, this.createExecutorService(sce));
        sce.getServletContext().setAttribute(COMMAND_RECEIVER_ATTRIBUTE, this.createCommandReceiver(sce));
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        ExecutorService executor = (ExecutorService) sce.getServletContext().getAttribute(EXECUTOR_ATTRIBUTE);
        executor.shutdown();
    }

    /**
     * Override to create a custom executor for Async servlets
     * @param sce
     * @return
     */
    protected ExecutorService createExecutorService(final ServletContextEvent sce) {
        return new ForkJoinPool();
    }

    protected abstract CommandReceiver createCommandReceiver(final ServletContextEvent sce);
}
