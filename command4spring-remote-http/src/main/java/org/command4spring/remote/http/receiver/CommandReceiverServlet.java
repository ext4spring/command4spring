package org.command4spring.remote.http.receiver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.remote.receiver.CommandReceiver;

public abstract class CommandReceiverServlet extends HttpServlet {

    private static final Log LOGGER = LogFactory.getLog(CommandReceiverServlet.class);
    private static final long serialVersionUID = 1L;
    public static final String COMMAND_RECEIVER_ATTRIBUTE = "command_receiver";
    private CommandReceiver commandReceiver;
    private ExecutorService executorService;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.isAsyncSupported()) {
            final AsyncContext acontext = req.startAsync();
            acontext.addListener(new AsyncListener() {

                @Override
                public void onTimeout(final AsyncEvent event) throws IOException {
                    //TODO: send propert error message to the caller
                    LOGGER.error("Request timed out");
                    ServletResponse response = event.getAsyncContext().getResponse();
                    PrintWriter out = response.getWriter();
                    out.write("TimeOut Error in Processing");
                }

                @Override
                public void onStartAsync(final AsyncEvent event) throws IOException {
                    LOGGER.debug("Async request processing started");
                }

                @Override
                public void onError(final AsyncEvent event) throws IOException {
                    //TODO: how to handle remote HTTP errors? 
                    LOGGER.error("Request error out");

                }

                @Override
                public void onComplete(final AsyncEvent event) throws IOException {
                    LOGGER.debug("Async request processing finished");
                }
            });
            AsyncServletProcess asyncServletProcess=new AsyncServletProcess(acontext, this.commandReceiver);
            this.executorService.execute(asyncServletProcess);
        } else {
            throw new ServletException("Servlet only works in async enabled mode.");
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Command receiver listening...");
        resp.getWriter().flush();
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        this.commandReceiver = this.getCommandReceiver(config);
        this.executorService=this.getExecutotService(config);
    }

    protected CommandReceiver getCommandReceiver(final ServletConfig config) throws ServletException {
        Object commandReceiverAttribute = config.getServletContext().getAttribute(COMMAND_RECEIVER_ATTRIBUTE);
        if (commandReceiverAttribute == null) {
            throw new ServletException("Couldn't initialize CommandReceiverServlet cause the servlet context attribute:" + COMMAND_RECEIVER_ATTRIBUTE
                    + " was null. Please extend CommandReceiverListener and set it as a servlet listener");
        } else if (!(commandReceiverAttribute instanceof CommandReceiver)) {
            throw new ServletException("Couldn't initialize CommandReceiverServlet cause the servlet context attribute:" + COMMAND_RECEIVER_ATTRIBUTE + " is not instance of CommandReceiver.");
        }
        return (CommandReceiver) commandReceiverAttribute;
    }

    protected ExecutorService getExecutotService(final ServletConfig config) throws ServletException {
        return new ForkJoinPool();
    }
}
