package org.command4spring.remote.http.receiver;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.remote.receiver.CommandReceiver;

public abstract class AbstractHttpCommandReceiverServlet extends HttpServlet {

    private static final Log LOGGER = LogFactory.getLog(AbstractHttpCommandReceiverServlet.class);
    private static final long serialVersionUID = 1L;

    private CommandReceiver commandReceiver;

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    protected void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("message received");
        try {
            TextDispatcherCommand textDispatcherCommand = this.parseRequest(req);
            TextDispatcherResult textDispatcherResult = this.commandReceiver.execute(textDispatcherCommand);
            this.writeResponse(resp, textDispatcherResult);
        } catch (DispatchException e) {
            LOGGER.error(e, e);
        }
    }

    protected void writeResponse(final ServletResponse response, final TextDispatcherResult textDispatcherResult) throws DispatchException {
        try {
            IOUtils.write(textDispatcherResult.getTextResult(), response.getOutputStream());
            for (String headerName:textDispatcherResult.getHeaders().keySet()) {
                ((HttpServletResponse)response).setHeader(headerName, textDispatcherResult.getHeader(headerName));
            }
        } catch (IOException e) {
            throw new DispatchException("Error while writing error to response:" + e, e);
        }
    }

    public TextDispatcherCommand parseRequest(final HttpServletRequest httpRequest) throws DispatchException {
        try {
            String textCommand = IOUtils.toString(httpRequest.getInputStream());
            TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(textCommand);
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            String headerName;
            while (headerNames.hasMoreElements()) {
                headerName = headerNames.nextElement();
                textDispatcherCommand.setHeader(headerName, httpRequest.getHeader(headerName));
            }
            return textDispatcherCommand;
        } catch (IOException e) {
            throw new DispatchException("Error while convert http request into Command object:" + e, e);
        }
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        this.commandReceiver = this.initCommandReceiver(config);
    }

    protected abstract CommandReceiver initCommandReceiver(final ServletConfig config) throws ServletException;

}
