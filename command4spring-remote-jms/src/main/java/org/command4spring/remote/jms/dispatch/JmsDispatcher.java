package org.command4spring.remote.jms.dispatch;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.AbstractRemoteDispatcher;
import org.command4spring.remote.model.CommandMessage;
import org.command4spring.remote.model.ResultMessage;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

public class JmsDispatcher extends AbstractRemoteDispatcher {

    private static final Log LOGGER = LogFactory.getLog(JmsDispatcher.class);

    private final JmsTemplate commandJmsTemplate;
    private final JmsTemplate resultJmsTemplate;

    private final long timeout = 5000;

    public JmsDispatcher(final JmsTemplate commandJmsTemplate, final JmsTemplate resultJmsTemplate, final Serializer serializer) {
        super(serializer);
        this.commandJmsTemplate = commandJmsTemplate;
        this.resultJmsTemplate = resultJmsTemplate;
    }

    @Override
    protected ResultMessage executeRemote(final CommandMessage commandMessage) throws DispatchException {
        final Command<? extends Result> command = commandMessage.getCommand();
        LOGGER.debug("Dispatching command through JMS. Command ID:" + command.getCommandId());
        try {
            this.commandJmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(final Session session) throws JMSException {
                    TextMessage message = session.createTextMessage();
                    message.setJMSExpiration(JmsDispatcher.this.timeout);
                    for (String headerKey : commandMessage.getHeaders().keySet()) {
                        message.setStringProperty(headerKey, commandMessage.getHeaders().get(headerKey));
                    }
                    message.setJMSMessageID(command.getCommandId());
                    message.setText(commandMessage.getTextCommand());
                    return message;
                }
            });
            if (commandMessage.isNoResultCommand()) {
                LOGGER.debug("Command sent through JMS. Command ID:" + command.getCommandId() + ". Doesn't wait for result");
                return new ResultMessage(null, commandMessage.getResultType());
            } else {
                LOGGER.debug("Command sent through JMS. Waiting for result. Command ID:" + command.getCommandId());
                TextMessage jmsResultMessage = this.resultJmsTemplate.receive("JMSCorrelationID='" + command.getCommandId() + "'", TextMessage.class, this.timeout);
                LOGGER.debug("Result received. Command ID:" + command.getCommandId());
                String textResult = jmsResultMessage.getText();
                ResultMessage resultMessage = new ResultMessage(textResult, commandMessage.getResultType());
                Enumeration<String> propertyNames = jmsResultMessage.getPropertyNames();
                while (propertyNames.hasMoreElements()) {
                    String headerName = propertyNames.nextElement();
                    resultMessage.setHeader(headerName, jmsResultMessage.getStringProperty(headerName));
                }
                return resultMessage;
            }
        } catch (JMSException e) {
            throw new DispatchException("Error while dispatching command through JMS:" + e, e);
        }
    }
}
