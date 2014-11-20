package org.command4spring.remote.jms.dispatch;

import java.util.concurrent.locks.ReentrantLock;

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
    private final ReentrantLock lock = new ReentrantLock();
    private Connection connection;
    private Session session;

    public JmsTemplate(final ConnectionFactory connectionFactory, final Destination destination) {
        super();
        this.connectionFactory = connectionFactory;
        this.destination = destination;
    }

    public void send(final MessageCreator messageCreator) throws JMSException {
        createProducer().send(messageCreator.createMessage(session));
    }

    @SuppressWarnings("unchecked")
    public <T extends Message> T receive(final String messageSelector, final Class<T> messageType, final long timeout) throws JMSException {
        Connection connection = createConnection();
        try {
            Session session = createSession(connection);
            MessageConsumer consumer = session.createConsumer(destination, messageSelector);
            T resultMessage = (T) consumer.receive(timeout);
            return resultMessage;
        } finally {
            closeConnection(connection);
        }

    }

    protected MessageProducer createProducer() throws JMSException {
        lock.lock();
        try {
            if (session != null) {
                try {
                    return session.createProducer(destination);
                } catch (Exception e) {
                    try {
                        connection.close();
                    } catch (Exception e1) {
                        //nop
                    }
                    connection = null;
                    session = null;
                }
            }
            if (connection == null) {
                connection = connectionFactory.createConnection();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            }
            return session.createProducer(destination);
        } finally {
            lock.unlock();
        }
    }

    protected Session createSession(final Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    protected Connection createConnection() throws JMSException {
        return connectionFactory.createConnection();
    }

    protected void closeConnection(final Connection connection) throws JMSException {
        connection.close();
    }

}
