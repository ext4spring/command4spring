package org.command4spring.remote.jms.dispatch;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.DispatcherFactory;
import org.command4spring.remote.model.DispatcherUrl;
import org.command4spring.remote.serializer.SerializerFactory;

public class JmsDispatcherFactory implements DispatcherFactory {

    private final SerializerFactory serializerFactory;
    private final ConnectionFactory connectionFactory;

    public JmsDispatcherFactory(ConnectionFactory connectionFactory, SerializerFactory serializerFactory) {
	super();
	this.serializerFactory = serializerFactory;
	this.connectionFactory = connectionFactory;
    }

    @Override
    public Dispatcher create(DispatcherUrl url) throws DispatchException {
	if (isFactoryFor(url)) {
	    Destination commandDestination = new ActiveMQQueue(url.getParameters().get("commandQueue"));
	    JmsTemplate commandJmsTemplate = new JmsTemplate(connectionFactory, commandDestination);
	    Destination resultDestination = new ActiveMQQueue(url.getParameters().get("resultQueue"));
	    JmsTemplate resultJmsTemplate = new JmsTemplate(connectionFactory, resultDestination);
	    return new JmsDispatcher(commandJmsTemplate, resultJmsTemplate, serializerFactory.create(url.getSerializer()));
	}
	throw new DispatchException("Cannot create JMS dispatcher for url:" + url);
    }

    protected String createTargetUrl(DispatcherUrl url) {
	return url.getProtocol() + "://" + url.getHostAndPort() + "/" + url.getPath();
    }

    @Override
    public boolean isFactoryFor(DispatcherUrl url) {
	return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }
}
