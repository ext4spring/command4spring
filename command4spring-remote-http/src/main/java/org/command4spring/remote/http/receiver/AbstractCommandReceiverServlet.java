package org.command4spring.remote.http.receiver;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.AsyncInterruptedException;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

public abstract class AbstractCommandReceiverServlet extends HttpServlet {

    private static final Log LOGGER = LogFactory.getLog(AbstractCommandReceiverServlet.class);
    private static final long serialVersionUID = 1L;
    public static final String DISPATCHER_ATTRIBUTE = "dispatcher";
    public static final String SERIALIZER_ATTRIBUTE = "serializer";
    public static final String ASYNC_WORKER_ATTRIBUTE = "async_worker";
    private Serializer serializer;
    private Dispatcher dispatcher;
    private AsyncServletWorker asyncWorker;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
	LOGGER.debug("message received");
	if (req.isAsyncSupported()) {
	    final AsyncContext acontext = req.startAsync();
	    LOGGER.debug("async context started");
	    try {
		String textCommand = IOUtils.toString(acontext.getRequest().getInputStream());
		Command<? extends Result> command = this.serializer.toCommand(textCommand);
		AsyncServletProcess asyncServletProcess = new AsyncServletProcess(acontext, command,  this.serializer, this.dispatcher);
		this.asyncWorker.queue(asyncServletProcess);
	    } catch (AsyncInterruptedException | CommandSerializationException e) {
		throw new ServletException(e.getMessage(), e);
	    }
	} else {
	    throw new ServletException("Servlet only works in async enabled mode.");
	}
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
	resp.getWriter().print("Command receiver listening...");
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
	super.init(config);
	this.asyncWorker = this.initAsyncWorker(config);
	this.serializer = initSerializer(config);
	this.dispatcher = initDispatcher(config);
	// TODO: add this to listener and the worker should receive from queue
	// on multiple threads since it is a bottleneck
	new Thread(this.asyncWorker).start();
	// TODO: stop on context stop
    }

    protected AsyncServletWorker initAsyncWorker(final ServletConfig config) throws ServletException {
	Object asyncWorkerAttribute = config.getServletContext().getAttribute(ASYNC_WORKER_ATTRIBUTE);
	if (asyncWorkerAttribute != null) {
	    return (AsyncServletWorker) asyncWorkerAttribute;
	}
	return new AsyncServletWorker();
    }

    protected abstract Serializer initSerializer(final ServletConfig config) throws ServletException;

    protected abstract Dispatcher initDispatcher(final ServletConfig config) throws ServletException;

}
