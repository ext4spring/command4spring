package org.command4spring.spring.boot;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.command4spring.remote.jms.dispatch.JmsTemplate;
import org.command4spring.remote.jms.receiver.JmsCommandReceiverListener;
import org.command4spring.spring.config.DefaultReceiverConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@ConfigurationProperties(ignoreUnknownFields = true, prefix = "command4spring.remote.jms")
public class EnableJmsCommandReceiver extends DefaultReceiverConfiguration {

    private static final String DEFAULT_BROKER_URL="tcp://0.0.0.0:0?persistent=false";
    String brokerUrl;

    @Bean
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.addConnector(DEFAULT_BROKER_URL);
        broker.start();
        return this.broker();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        if (this.brokerUrl != null) {
            return new CachingConnectionFactory(new ActiveMQConnectionFactory(this.brokerUrl));
        }
        return new CachingConnectionFactory(new ActiveMQConnectionFactory(DEFAULT_BROKER_URL));
    }

    @Bean
    public Destination commandDestination() {
        return new ActiveMQQueue("COMMAND.QUEUE");
    }

    @Bean
    public Destination resultDestination() {
        return new ActiveMQQueue("RESULT.QUEUE");
    }

    @Bean
    public JmsTemplate resultJmsTemplate() {
        return new JmsTemplate(this.connectionFactory(), this.resultDestination());
    }

    @Bean
    public JmsCommandReceiverListener jmsCommandReceiverListener() {
        return new JmsCommandReceiverListener(this.commandReceiver(), this.resultJmsTemplate());
    }

    @Bean
    public DefaultMessageListenerContainer commandMessageListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setMessageListener(this.jmsCommandReceiverListener());
        container.setDestination(this.commandDestination());
        container.setConnectionFactory(this.connectionFactory());
        return container;
    }
}
