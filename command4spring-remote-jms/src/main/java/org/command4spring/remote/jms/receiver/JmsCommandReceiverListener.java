package org.command4spring.remote.jms.receiver;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.jms.dispatch.JmsTemplate;
import org.command4spring.remote.jms.dispatch.MessageCreator;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;
import org.command4spring.remote.receiver.CommandReceiver;

/**
 * Configure this as a message listener for the queue where you send commands
 * with {@link JmsDispatcherService}
 * 
 * @author borbasp
 */
public class JmsCommandReceiverListener implements MessageListener {
    private static final Log LOGGER = LogFactory.getLog(JmsCommandReceiverListener.class);

    private final CommandReceiver commandReceiver;
    private final JmsTemplate resultJmsTemplate;

    private long timeout = 5000;

    public JmsCommandReceiverListener(final CommandReceiver commandReceiver, final JmsTemplate resultJmsTemplate) {
	super();
	this.commandReceiver = commandReceiver;
	this.resultJmsTemplate = resultJmsTemplate;
    }

    @Override
    public void onMessage(final Message message) {
	try {
	    TextDispatcherCommand commandMessage = this.parseReceivedMessage(message);
	    TextDispatcherResult resultMessage;
	    resultMessage = commandReceiver.execute(commandMessage);
	    if (resultMessage.getTextResult() == null) {
		LOGGER.debug("Message processed, empty result, no need for response message");
	    } else {
		this.sendResult(resultMessage);
	    }
	} catch (DispatchException | JMSException e) {
	    LOGGER.error("Error while dispatchig message received through JMS: " + e, e);
	}
    }

    @SuppressWarnings("unchecked")
    private TextDispatcherCommand parseReceivedMessage(Message message) throws DispatchException {
	if (message instanceof TextMessage) {
	    try {
		TextMessage textMessage = (TextMessage) message;
		String textCommand = textMessage.getText();
		TextDispatcherCommand commandMessage = new TextDispatcherCommand(textCommand);
		Enumeration<String> propertyNames = textMessage.getPropertyNames();
		String headerName;
		while (propertyNames.hasMoreElements()) {
		    headerName = propertyNames.nextElement();
		    commandMessage.setHeader(headerName, textMessage.getStringProperty(headerName));
		}
		return commandMessage;
	    } catch (JMSException e) {
		throw new DispatchException("Error while parsing JMS message:" + e, e);
	    }
	} else {
	    throw new DispatchException("Invalid message type. Only TextMessage is supported");
	}

    }

    private void sendResult(final TextDispatcherResult resultMessage) throws JMSException, CommandSerializationException {
	LOGGER.debug("Command dispatched. Sending to result queue.");
	this.resultJmsTemplate.send(new MessageCreator() {
	    @Override
	    public Message createMessage(final Session session) throws JMSException {
		TextMessage msg = session.createTextMessage();
		msg.setText(resultMessage.getTextResult());
		msg.setJMSTimestamp(System.currentTimeMillis());
		msg.setJMSCorrelationID(resultMessage.getCommandId());
		msg.setJMSExpiration(JmsCommandReceiverListener.this.timeout);
		for (String headerName : resultMessage.getHeaders().keySet()) {
		    msg.setStringProperty(headerName, resultMessage.getHeader(headerName));
		}
		return msg;
	    }
	});
    }

    public void setTimeout(final long timeout) {
	this.timeout = timeout;
    }

    public long getTimeout() {
	return this.timeout;
    }
}
