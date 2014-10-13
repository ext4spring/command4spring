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
import org.command4spring.remote.jms.dispatch.JmsDispatcher;
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
            try {
                String textCommand = textMessage.getText();
                Command<? extends Result> command = this.serializer.toCommand(textCommand);
                Result result = this.dispatcher.dispatch(command).getResult(this.timeout, TimeUnit.MILLISECONDS);
                if (result instanceof NoResult) {
                    LOGGER.debug("Message processed, client needs no result message since type is NoResult.");
                } else {
                    this.sendResult(result);
                }
            } catch (Exception e) {
                // TODO send error message
                LOGGER.error("Error while processing message:" + e, e);
            }
        } else {
            // TODO send error message
            LOGGER.error("Only ObjectMessage is supported by this listener");
        }
    }

    private void sendResult(final Result result) throws JMSException, CommandSerializationException {
        LOGGER.debug("Command dispatched. Sending to result queue. Result: " + result);
        final String textResult=this.serializer.toText(result);
        this.resultJmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(final Session session) throws JMSException {
                TextMessage msg = session.createTextMessage();
                msg.setText(textResult);
                msg.setJMSTimestamp(System.currentTimeMillis());
                msg.setJMSCorrelationID(result.getCommandId());
                msg.setStringProperty(JmsDispatcher.COMMAND_CLASS_HEADER, result.getClass().getName());
                msg.setStringProperty(JmsDispatcher.COMMAND_ID_HEADER, result.getCommandId());
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
