package org.command4spring.sample.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.remote.http.mapper.HttpMapper;
import org.command4spring.remote.http.mapper.RestHttpMapper;
import org.command4spring.serializer.Serializer;
import org.command4spring.spring.dispatcher.SpringInVmDispatcher;
import org.command4spring.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public Serializer serializer() {
	return new XmlSerializer();
    }

    @Bean
    public HttpMapper httpMapper() {
	return new RestHttpMapper(serializer());
    }

    @Bean
    public ExecutorService executor() {
	return new ThreadPoolExecutor(8, 50, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

    @Bean
    public Dispatcher dispatcher() {
	return new SpringInVmDispatcher(executor());
    }
}