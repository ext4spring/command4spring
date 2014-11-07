package org.command4spring.spring.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.remote.receiver.CommandReceiver;
import org.command4spring.remote.receiver.DefaultCommandReceiver;
import org.command4spring.serializer.Serializer;
import org.command4spring.spring.dispatcher.SpringInVmDispatcher;
import org.command4spring.spring.remote.http.receiver.SpringCommandReceiverSerlvet;
import org.command4spring.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;

public class DefaultHttpReceiverConfiguration {

    @Bean
    public Servlet dispatcherServlet() {
        return new SpringCommandReceiverSerlvet();
    }

    @Bean
    public Serializer serializer() {
        return new XmlSerializer();
    }

    @Bean
    public ExecutorService executor() {
        return new ThreadPoolExecutor(8, 50, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

    @Bean
    public ChainableDispatcher dispatcher() {
        SpringInVmDispatcher dispatcher = new SpringInVmDispatcher();
        dispatcher.setExecutorService(this.executor());
        return dispatcher;
    }

    @Bean
    public CommandReceiver commandReceiver() {
        return new DefaultCommandReceiver(this.serializer(), this.dispatcher());
    }
}
