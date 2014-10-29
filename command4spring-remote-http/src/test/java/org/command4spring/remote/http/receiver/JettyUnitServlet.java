package org.command4spring.remote.http.receiver;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyUnitServlet {

    public static Server startServer(final Class<? extends Servlet> servletClass, final int port, final String path) throws Exception {
        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(servletClass, path);
        server.start();
        return server;
    }
}
