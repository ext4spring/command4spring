package org.command4spring.spring.remote.http.receiver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.remote.http.mapper.HttpMapper;
import org.command4spring.remote.http.receiver.AbstractHttpCommandReceiverServlet;
import org.command4spring.serializer.Serializer;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class SpringCommandReceiverSerlvet extends AbstractHttpCommandReceiverServlet {

    @Override
    protected Dispatcher initDispatcher(ServletConfig config) throws ServletException {
	Dispatcher dispatcher;
	String beanName = config.getInitParameter(DISPATCHER_ATTRIBUTE);
	if (beanName == null) {
	    dispatcher = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(Dispatcher.class);
	} else {
	    dispatcher = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, Dispatcher.class);
	}
	return dispatcher;
    }

    @Override
    protected HttpMapper initHttpMapper(ServletConfig config) throws ServletException {
	HttpMapper httpMapper;
	String beanName = config.getInitParameter(HTTP_MAPPER_ATTRIBUTE);
	if (beanName == null) {
	    httpMapper = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(HttpMapper.class);
	} else {
	    httpMapper = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, HttpMapper.class);
	}
	return httpMapper;
    }

}
