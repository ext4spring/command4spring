package org.ext4spring.sample.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.remote.http.dispatcher.RestHttpDispatcher;
import org.command4spring.result.ResultFuture;
import org.command4spring.result.StringResult;
import org.command4spring.sample.common.command.GetTimeCommand;
import org.command4spring.xml.serializer.XmlSerializer;

/**
 * Servlet implementation class GetTimeServlet
 */
@WebServlet(urlPatterns = "/time")
public class GetTimeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Log LOGGER = LogFactory.getLog(GetTimeServlet.class);
    private final Dispatcher httpDispatcher;

    /**
     * Default constructor.
     */
    public GetTimeServlet() {
        this.httpDispatcher=new RestHttpDispatcher("http://localhost:8080/command4spring-sample-service/execute",new XmlSerializer());
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("direct") != null) {
                String time = "" + System.currentTimeMillis();
                response.getWriter().write(time);
            } else {
                LOGGER.debug("Incoming request");
                GetTimeCommand getTimeCommand = new GetTimeCommand();
                ResultFuture<StringResult> getTimeResult = this.httpDispatcher.dispatch(getTimeCommand);
                LOGGER.debug("Command sent, waiting for response");
                String time = getTimeResult.getResult().getValue();
                LOGGER.debug("Result:" + time);
                response.getWriter().write(time);
            }
        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }

}
