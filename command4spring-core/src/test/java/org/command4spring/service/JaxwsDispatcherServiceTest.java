package org.command4spring.service;

import javax.xml.ws.Endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-c4s-core-test.xml"})
public class JaxwsDispatcherServiceTest {

    @Autowired
    JaxwsDispatcherService jaxwsDispatcherService;
    
    @Test
    public void testWsInit() {
        Endpoint.publish("http://localhost:9999/dispatcher", jaxwsDispatcherService);
    }
    
}
