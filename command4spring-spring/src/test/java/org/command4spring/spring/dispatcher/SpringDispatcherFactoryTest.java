package org.command4spring.spring.dispatcher;

import org.command4spring.remote.http.dispatcher.RestHttpDispatcherFactory;
import org.command4spring.spring.dispatcher.serializer.SpringSerializerFactory;
import org.command4spring.xml.serializer.XmlSerializerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class SpringDispatcherFactoryTest {

    @Configuration
    static class ContextConfiguration {

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
        public SpringDispatcherFactory springDispatcherFactory() {
            return new SpringDispatcherFactory();
        }

    }

    @Autowired
    SpringSerializerFactory serializerFactory;

    @Autowired
    SpringDispatcherFactory dispatcherFactory;

    @Test
    public void testFactoryAutowire() {
        Assert.assertEquals(1, serializerFactory.getFactories().size());
        Assert.assertTrue(serializerFactory.getFactories().get(0) instanceof XmlSerializerFactory);
        Assert.assertEquals(1, dispatcherFactory.getFactories().size());
        Assert.assertTrue(dispatcherFactory.getFactories().get(0) instanceof RestHttpDispatcherFactory);
    }


}
