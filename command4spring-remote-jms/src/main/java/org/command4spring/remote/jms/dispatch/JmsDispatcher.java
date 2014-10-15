package org.command4spring.remote.jms.dispatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
import org.command4spring.util.CommandUtil;

public class JmsDispatcher extends AbstractDispatcher {

    private static final Log LOGGER = LogFactory.getLog(JmsDispatcher.class);

    private final JmsTemplate commandJmsTemplate;
    private final JmsTemplate resultJmsTemplate;
    private final Serializer serializer;

    private final long timeout = 5000;

    public JmsDispatcher(final JmsTemplate commandJmsTemplate, final JmsTemplate resultJmsTemplate, final Serializer serializer) {
        this.commandJmsTemplate = commandJmsTemplate;
        this.resultJmsTemplate = resultJmsTemplate;
        this.serializer = serializer;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException {
        LOGGER.debug("Dispatching command through JMS. Command ID:" + command.getCommandId());
        try {
            final String textMessage = JmsDispatcher.this.serializer.toText(command);
            this.commandJmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(final Session session) throws JMSException {
                    TextMessage message = session.createTextMessage();
                    message.setJMSExpiration(JmsDispatcher.this.timeout);
                    message.setStringProperty(JmsHeader.COMMAND_CLASS_HEADER, command.getClass().getName());
                    message.setStringProperty(JmsHeader.COMMAND_ID_HEADER, command.getCommandId());
                    message.setJMSMessageID(command.getCommandId());
                    message.setText(textMessage);
                    return message;
                }
            });
            if (CommandUtil.isNoResultCommand(command)) {
                LOGGER.debug("Command sent through JMS. Command ID:" + command.getCommandId());
                return (R) new NoResult(command.getCommandId());
            } else {
                LOGGER.debug("Command sent through JMS. Waiting for result. Command ID:" + command.getCommandId());
                TextMessage resultMessage = this.resultJmsTemplate.receive("JMSCorrelationID='" + command.getCommandId() + "'", TextMessage.class, this.timeout);
                if (resultMessage.getStringProperty(JmsHeader.RESULT_EXCEPTION_CLASS) != null) {
                    throw new RemoteDispatchException(resultMessage.getText());
                } else {
                    String textResult = resultMessage.getText();
                    R result = (R) this.serializer.toResult(textResult);
                    LOGGER.debug("Result received. Command ID:" + command.getCommandId());
                    return result;
                }
            }
        } catch (JMSException e) {
            throw new DispatchException("Error while dispatching command through JMS:" + e, e);
        }
    }
}
