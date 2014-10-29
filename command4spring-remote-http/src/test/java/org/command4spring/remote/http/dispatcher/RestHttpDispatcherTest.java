package org.command4spring.remote.http.dispatcher;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.remote.http.receiver.JettyUnitServlet;
import org.command4spring.remote.http.receiver.TestHttpReceiverServlet;
import org.command4spring.testbase.test.AbstractDispatcherTest;
import org.command4spring.xml.serializer.XmlSerializer;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class RestHttpDispatcherTest extends AbstractDispatcherTest {

    static Server server;

    final RestHttpDispatcher dispatcher;

    public RestHttpDispatcherTest() {
        this.dispatcher = new RestHttpDispatcher("http://localhost:8123/", new XmlSerializer());
        this.dispatcher.setTimeout(1000);
    }

    @BeforeClass
    public static void startServer() throws Exception {
        server = JettyUnitServlet.startServer(TestHttpReceiverServlet.class, 8123, "/*");
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Override
    protected ChainableDispatcher getDispatcher() {
        return this.dispatcher;
    }

}
