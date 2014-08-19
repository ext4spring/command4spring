package org.command4spring.jms.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.command4spring.command.Command;
import org.command4spring.result.Result;
import org.command4spring.service.AbstractDispatcherService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
/**
 * Configure this as a message listener for the queue where you send commands with {@link JmsDispatcherService}
 * @author borbasp
 *
 */
public class DispatcherMessageListener extends AbstractDispatcherService implements MessageListener {
	private static final Logger LOGGER = Logger.getLogger(DispatcherMessageListener.class);

	private JmsTemplate resultJmsTemplate;
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				@SuppressWarnings("unchecked")
				Command<? extends Result> command = (Command<? extends Result>) objectMessage.getObject();
				Result result = this.getDispatcher().dispatch(command).get();
				this.sendResult(result);
			} catch (Exception e) {
				throw new ListenerExecutionFailedException("Error while dispatching command:" + e, e);
			}
		} else {
			LOGGER.error("Only ObjectMessage is supported by this listener");
		}
	}
	
	private void sendResult(final Result result) {
		if (this.resultJmsTemplate==null) {
			LOGGER.info("Command dispatched. Result wasted (no result jms template configured). Result was: " + result);
		} else {
			LOGGER.info("Command dispatched. Sending to result queue. Result: " + result);
			this.resultJmsTemplate.send(new MessageCreator() {
	            @Override
	            public Message createMessage(Session session) throws JMSException {
	                ObjectMessage msg = session.createObjectMessage(result);
	                msg.setJMSTimestamp(System.currentTimeMillis());
	                msg.setJMSCorrelationID(result.getCommandId());
	                msg.setStringProperty("result.getClass().getName()", result.getClass().getName());
	                msg.setStringProperty("result.toString()", result.toString());
	                return msg;
	            }
	        });
		}
	}
	
	public void setResultJmsTemplate(JmsTemplate resultJmsTemplate) {
		this.resultJmsTemplate = resultJmsTemplate;
	}

}
