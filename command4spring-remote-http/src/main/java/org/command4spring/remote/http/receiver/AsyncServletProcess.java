package org.command4spring.remote.http.receiver;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.receiver.CommandReceiver;

public class AsyncServletProcess implements Runnable {
    private static final Log LOGGER = LogFactory.getLog(AsyncServletProcess.class);

    private final AsyncContext asyncContext;
    private final CommandReceiver commandReceiver;

    public AsyncServletProcess(final AsyncContext asyncContext, final CommandReceiver commandReceiver) {
        this.asyncContext=asyncContext;
        this.commandReceiver=commandReceiver;
    }

    @Override
    public void run() {
        this.execute((HttpServletRequest)this.asyncContext.getRequest(), (HttpServletResponse)this.asyncContext.getRequest());
        this.asyncContext.complete();
    }

    /**
     * Executes the request with the {@link CommandReceiver}
     * @param req
     * @param resp
     */
    protected void execute(final HttpServletRequest req, final HttpServletResponse resp) {
        try {
            try {
                String textCommand = IOUtils.toString(req.getInputStream());
                String textResult = this.commandReceiver.receive(textCommand);
                IOUtils.write(textResult, resp.getOutputStream());
                resp.getOutputStream().flush();
            } catch (DispatchException e) {
                LOGGER.error("Error while processing request:" + e.getMessage(), e);
                IOUtils.write(e.getMessage(), resp.getOutputStream());
            }
        } catch (IOException e) {
            LOGGER.error("Error while processing request:" + e.getMessage(), e);
        }
    }


}
