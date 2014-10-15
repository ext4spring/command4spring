package org.command4spring.remote.jms.receiver;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.jms.dispatch.JmsHeader;
import org.command4spring.remote.jms.dispatch.JmsTemplate;
import org.command4spring.remote.jms.dispatch.MessageCreator;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

/**
 * Configure this as a message listener for the queue where you send commands with {@link JmsDispatcherService}
 * 
 * @author borbasp
 */
public class JmsCommandReceiverListener implements MessageListener {
    private static final Logger LOGGER = Logger.getLogger(JmsCommandReceiverListener.class);

    private final Dispatcher dispatcher;
    private final Serializer serializer;
    private final JmsTemplate resultJmsTemplate;

    private long timeout = 5000;

    public JmsCommandReceiverListener(final Dispatcher dispatcher, final Serializer serializer, final JmsTemplate resultJmsTemplate) {
        super();
        this.dispatcher = dispatcher;
        this.serializer = serializer;
        this.resultJmsTemplate = resultJmsTemplate;
    }

    @Override
    public void onMessage(final Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            Command<? extends Result> command = null;
            try {
                String textCommand = textMessage.getText();
                command = this.serializer.toCommand(textCommand);
                Result result = this.dispatcher.dispatch(command).getResult(this.timeout, TimeUnit.MILLISECONDS);
                if (result instanceof NoResult) {
                    LOGGER.debug("Message processed, client needs no result message since type is NoResult.");
                } else {
                    this.sendResult(result);
                }
            } catch (Throwable e) {
                this.sendError(command, e);
            }
        } else {
            this.sendError(null, new DispatchException("Only text message is supported"));
        }
    }

    private void sendError(final Command<? extends Result> command, final Throwable t) {
        LOGGER.error("Error while dispatching command:" + t, t);
        try {
            this.resultJmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(final Session session) throws JMSException {
                    TextMessage msg = session.createTextMessage();
                    msg.setJMSTimestamp(System.currentTimeMillis());
                    if (command != null) {
                        msg.setJMSCorrelationID(command.getCommandId());
                        msg.setStringProperty(JmsHeader.COMMAND_CLASS_HEADER, command.getClass().getName());
                        msg.setStringProperty(JmsHeader.COMMAND_ID_HEADER, command.getCommandId());
                    }
                    msg.setJMSExpiration(JmsCommandReceiverListener.this.timeout);
                    msg.setStringProperty(JmsHeader.RESULT_EXCEPTION_CLASS, t.getClass().getName());
                    msg.setText(t.getMessage());
                    return msg;
                }
            });
        } catch (JMSException e) {
            LOGGER.error("Error while sending error response:" + e, e);
        }
    }

    private void sendResult(final Result result) throws JMSException, CommandSerializationException {
        LOGGER.debug("Command dispatched. Sending to result queue. Result: " + result);
        final String textResult = this.serializer.toText(result);
        this.resultJmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(final Session session) throws JMSException {
                TextMessage msg = session.createTextMessage();
                msg.setText(textResult);
                msg.setJMSTimestamp(System.currentTimeMillis());
                msg.setJMSCorrelationID(result.getCommandId());
                msg.setJMSExpiration(JmsCommandReceiverListener.this.timeout);
                msg.setStringProperty(JmsHeader.COMMAND_CLASS_HEADER, result.getClass().getName());
                msg.setStringProperty(JmsHeader.COMMAND_ID_HEADER, result.getCommandId());
                msg.setStringProperty(JmsHeader.RESULT_CLASS, result.getClass().getName());
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
