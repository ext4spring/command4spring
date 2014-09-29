package org.ext4spring.sample.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.result.ResultFuture;
import org.command4spring.result.StringResult;
import org.command4spring.sample.common.command.GetTimeCommand;

/**
 * Servlet implementation class GetTimeServlet
 */
public class GetTimeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Dispatcher sampleDispatcher;

    /**
     * Default constructor. 
     */
    public GetTimeServlet() {
        // TODO Auto-generated constructor stub
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
