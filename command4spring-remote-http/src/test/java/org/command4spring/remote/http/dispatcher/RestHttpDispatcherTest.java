package org.command4spring.remote.http.dispatcher;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.http.receiver.JettyUnitServlet;
import org.command4spring.remote.http.receiver.TestHttpReceiverServlet;
import org.command4spring.xml.serializer.XmlSerializer;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RestHttpDispatcherTest {

    static Server server;

    @BeforeClass
    public static void startServer() throws Exception {
        server=JettyUnitServlet.startServer(TestHttpReceiverServlet.class, 8123, "/*");
    }

    @Test
    public void testSampleCommandExecution() throws InterruptedException, DispatchException {
        RestHttpDispatcher dispatcher=new RestHttpDispatcher("http://localhost:8123/", new XmlSerializer());
        SampleCommand sampleCommand=new SampleCommand("data");
        SampleResult result=dispatcher.dispatch(sampleCommand).getResult();
        Assert.assertNotNull(result);
        Assert.assertEquals(sampleCommand.getCommandId(), result.getCommandId());
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

}
