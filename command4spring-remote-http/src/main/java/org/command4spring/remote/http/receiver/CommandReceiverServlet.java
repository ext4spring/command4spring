package org.command4spring.remote.http.receiver;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.receiver.CommandReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class CommandReceiverServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CommandReceiver commandReceiver;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String textCommand = IOUtils.toString(req.getInputStream());
        try {
            String textResult = this.commandReceiver.receive(textCommand);
            IOUtils.write(textResult, resp.getOutputStream());
        } catch (DispatchException e) {
            IOUtils.write(e.getMessage(), resp.getOutputStream());
        }
        resp.getOutputStream().flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Command receiver listening...");
        resp.getWriter().flush();
    }
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }
}
