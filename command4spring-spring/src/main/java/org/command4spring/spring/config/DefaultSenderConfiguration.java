package org.command4spring.spring.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.command4spring.remote.http.dispatcher.RestHttpDispatcherFactory;
import org.command4spring.remote.jms.dispatch.JmsDispatcherFactory;
import org.command4spring.remote.serializer.Serializer;
import org.command4spring.spring.dispatcher.SpringDispatcherFactory;
import org.command4spring.spring.dispatcher.serializer.SpringSerializerFactory;
import org.command4spring.xml.serializer.XmlSerializer;
import org.command4spring.xml.serializer.XmlSerializerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultSenderConfiguration {

    @Bean
    public Serializer serializer() {
        return new XmlSerializer();
    }

    @Bean
    public XmlSerializerFactory xmlSerializerFactory() {
        return new XmlSerializerFactory();
    }

    @Bean
    public SpringSerializerFactory springSerializerFactory() {
        return new SpringSerializerFactory();
    }

    @Bean
    public RestHttpDispatcherFactory restHttpDispatcherFactory() {
        return new RestHttpDispatcherFactory(springSerializerFactory());
    }

    @Bean
    public JmsDispatcherFactory JmsDispatcherFactory() {
        return new JmsDispatcherFactory(springSerializerFactory());
    }

    @Bean
    public SpringDispatcherFactory springDispatcherFactory() {
        return new SpringDispatcherFactory();
    }

    @Bean
    public ExecutorService executor() {
        return new ThreadPoolExecutor(8, 50, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

}
