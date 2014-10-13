package org.command4spring.remote.jms.dispatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public interface MessageCreator {

    /**
     * Create a {@link Message} to be sent.
     * @param session the JMS {@link Session} to be used to create the
     * {@code Message} (never {@code null})
     * @return the {@code Message} to be sent
     * @throws javax.jms.JMSException if thrown by JMS API methods
     */
    Message createMessage(Session session) throws JMSException;

}