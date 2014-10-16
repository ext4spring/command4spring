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
import org.command4spring.sample.common.command.SleepCommand;
import org.command4spring.xml.serializer.XmlSerializer;

/**
 * Servlet implementation class GetTimeServlet
 */
@WebServlet(urlPatterns="/sleep")
public class SleepServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Log LOGGER=LogFactory.getLog(SleepServlet.class);
    private final Dispatcher httpDispatcher;

    /**
     * Default constructor. 
     */
    public SleepServlet() {
        this.httpDispatcher=new RestHttpDispatcher("http://localhost:8080/command4spring-sample-service/execute",new XmlSerializer());
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            long time=Long.parseLong(request.getParameter("time"));
            LOGGER.debug("Incoming request. sleep for:"+time);
            SleepCommand sleepCommand=new SleepCommand(time);
            this.httpDispatcher.dispatch(sleepCommand).getResult();
            response.getWriter().write("OK");
        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }

}
