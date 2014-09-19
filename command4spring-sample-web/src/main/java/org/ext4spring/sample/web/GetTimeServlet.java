package org.ext4spring.sample.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.command4spring.sample.common.command.GetTimeCommand;

/**
 * Servlet implementation class GetTimeServlet
 */
public class GetTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    GetTimeCommand getTimeCommand=new GetTimeCommand();
	    System.out.println("ahdlksadlksahdl");
	}

}
