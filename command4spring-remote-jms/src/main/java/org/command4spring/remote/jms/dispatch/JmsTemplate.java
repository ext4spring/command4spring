package org.command4spring.remote.jms.dispatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class JmsTemplate {
    private final ConnectionFactory connectionFactory;
    private final Destination destination;

    public JmsTemplate(final ConnectionFactory connectionFactory, final Destination destination) {
        super();
        this.connectionFactory = connectionFactory;
        this.destination = destination;
    }

    public void send(final MessageCreator messageCreator) throws JMSException  {
        Connection connection = this.createConnection();
        try {
            Session session = this.createSession(connection);
            MessageProducer producer = this.createProducer(session, this.destination);
            producer.send(messageCreator.createMessage(session));
        } finally {
            this.closeConnection(connection);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Message> T receive(final String messageSelector, final Class<T> messageType, final long timeout) throws JMSException {
        Connection connection = this.createConnection();
        try {
            Session session = this.createSession(connection);
            MessageConsumer consumer = session.createConsumer(this.destination, messageSelector);
            T resultMessage = (T) consumer.receive(timeout);
            return resultMessage;
        } finally {
            this.closeConnection(connection);
        }

    }

    protected MessageProducer createProducer(final Session session, final Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    protected Session createSession(final Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    protected Connection createConnection() throws JMSException {
        return this.connectionFactory.createConnection();
    }

    protected void closeConnection(final Connection connection) throws JMSException {
        connection.close(); 
    }

}
