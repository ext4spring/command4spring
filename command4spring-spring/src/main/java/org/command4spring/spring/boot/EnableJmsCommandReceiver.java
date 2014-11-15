package org.command4spring.spring.boot;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.command4spring.remote.jms.dispatch.JmsTemplate;
import org.command4spring.remote.jms.receiver.JmsCommandReceiverListener;
import org.command4spring.spring.config.DefaultReceiverConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class EnableJmsCommandReceiver extends DefaultReceiverConfiguration {

    @Autowired
    ConnectionFactory connectionFactory;

    @Bean
    public Destination commandDestination() {
        return new ActiveMQQueue("COMMAND.QUEUE");
    }

    @Bean
    public Destination resultDestination() {
        return new ActiveMQQueue("RESULT.QUEUE");
    }

    @Bean
    public JmsTemplate commandJmsTemplate() {
	return new JmsTemplate(connectionFactory, commandDestination());
    }

    @Bean
    public JmsTemplate resultJmsTemplate() {
        return new JmsTemplate(connectionFactory, resultDestination());
    }

    @Bean
    public JmsCommandReceiverListener jmsCommandReceiverListener() {
        return new JmsCommandReceiverListener(commandReceiver(), resultJmsTemplate());
    }

    @Bean
    public DefaultMessageListenerContainer commandMessageListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setMessageListener(jmsCommandReceiverListener());
        container.setDestination(commandDestination());
        container.setConnectionFactory(connectionFactory);
        return container;
    }


}
