package org.command4spring.remote.http.receiver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.exception.AsyncInterruptedException;

public class AsyncServletWorker implements Runnable {

    private static final Log LOGGER = LogFactory.getLog(AsyncServletWorker.class);
    private boolean running = true;
    private final LinkedBlockingQueue<AsyncServletProcess> processes = new LinkedBlockingQueue<AsyncServletProcess>();

    public void queue(AsyncServletProcess asyncServletProcess) throws AsyncInterruptedException {
	this.processes.offer(asyncServletProcess);
	LOGGER.debug("process enqueued. queue size:" + processes.size());
    }

    @Override
    public void run() {
	LOGGER.info("AsyncServletWorker started");
	while (this.running) {
	    try {
		AsyncServletProcess process = processes.poll(5, TimeUnit.SECONDS);
		if (process != null) {
		    LOGGER.debug("executing async process. queue size:"+processes.size());
		    process.execute();
		}
	    } catch (InterruptedException e) {
		LOGGER.info("AsyncServletWorker interrupted");
	    }
	}
	LOGGER.info("AsyncServletWorker stopped");	
    }

    public void stop() {
	this.running = false;
    }
}
