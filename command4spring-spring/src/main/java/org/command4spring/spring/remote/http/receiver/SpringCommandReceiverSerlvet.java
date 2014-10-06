package org.command4spring.spring.remote.http.receiver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.remote.http.receiver.AbstractCommandReceiverServlet;
import org.command4spring.serializer.Serializer;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class SpringCommandReceiverSerlvet extends AbstractCommandReceiverServlet {

    @Override
    protected Dispatcher initDispatcher(ServletConfig config) throws ServletException {
        Dispatcher dispatcher;
	String beanName=config.getInitParameter(DISPATCHER_ATTRIBUTE);
        if (beanName==null) {
            dispatcher=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(Dispatcher.class);
        } else {
            dispatcher=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, Dispatcher.class);
        }
        return dispatcher;
    }
    
    @Override
    protected Serializer initSerializer(ServletConfig config) throws ServletException {
        Serializer serializer;
	String beanName=config.getInitParameter(DISPATCHER_ATTRIBUTE);
        if (beanName==null) {
            serializer=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(Serializer.class);
        } else {
            serializer=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, Serializer.class);
        }
        return serializer;
    }
    
}
