package org.ext4spring.sample.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.remote.http.dispatcher.HttpDispatcher;
import org.command4spring.result.ResultFuture;
import org.command4spring.result.StringResult;
import org.command4spring.sample.common.command.GetTimeCommand;
import org.command4spring.xml.serializer.XmlSerializer;

/**
 * Servlet implementation class GetTimeServlet
 */
public class GetTimeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final Dispatcher sampleDispatcher;

    /**
     * Default constructor. 
     */
    public GetTimeServlet() {
        this.sampleDispatcher=new HttpDispatcher(new XmlSerializer(), "http://localhost:8080/execute");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            GetTimeCommand getTimeCommand=new GetTimeCommand();
            ResultFuture<StringResult> getTimeResult=this.sampleDispatcher.dispatch(getTimeCommand);

            String time=getTimeResult.getResult().getValue();
            response.getWriter().write(time);
        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }

}
