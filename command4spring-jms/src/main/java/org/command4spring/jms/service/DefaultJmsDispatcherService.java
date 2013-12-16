package org.command4spring.jms.service;

import java.util.concurrent.Future;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.jms.result.JmsEmptyFutureResult;
import org.command4spring.jms.result.JmsFutureResult;
import org.command4spring.jms.result.ResultMessageListener;
import org.command4spring.result.Result;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

//TODO: support text messages with XML and JSON formats (even in DispathcerMessageListener)

/**
 * Asynchronous JMS implementation of the {@link JmsDispatcherService} The
 * jmsTemplate defines the JMS queue to use. The
 * {@link DispatcherMessageListener} class should be registered as a Message
 * listener for the same queue
 */
public class DefaultJmsDispatcherService implements JmsDispatcherService {
	private static final Logger LOGGER = Logger.getLogger(DefaultJmsDispatcherService.class);

	private JmsTemplate jmsTemplate;
	private ResultMessageListener resultMessageListener;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <C extends Command<R>, R extends Result> Future<R> dispatch(final C command) throws DispatchException {
		Future<R> futureResult;
		if (this.resultMessageListener==null) {
			futureResult=new JmsEmptyFutureResult<R>();
		} else {
			futureResult=(JmsFutureResult<R>) this.resultMessageListener.handle(command.getCommandId());
		}		
	
		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage msg = session.createObjectMessage(command);
				msg.setJMSTimestamp(System.currentTimeMillis());
				msg.setJMSMessageID(command.getCommandId());
				msg.setStringProperty("command.getClass().getName()", command.getClass().getName());
				msg.setStringProperty("command.toString()", command.toString());
				return msg;
			}
		});
		LOGGER.debug("Command sent to dispatcher queue successfully: " + command);
		return futureResult;
	}
	
	@Required
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * Set this if you want to receive your result through JMS
	 * @param resultMessageListener
	 */
	public void setResultMessageListener(ResultMessageListener resultMessageListener) {
		this.resultMessageListener = resultMessageListener;
	}

}
