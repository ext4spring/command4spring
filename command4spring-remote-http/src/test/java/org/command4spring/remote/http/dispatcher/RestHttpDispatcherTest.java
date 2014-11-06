package org.command4spring.remote.http.dispatcher;

import java.util.concurrent.TimeUnit;

import org.command4spring.command.Command;
import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.example.SampleCommand;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.filter.circuit.CircuitBreakerFilter;
import org.command4spring.remote.dispatcher.filter.circuit.CircuiteBreakerException;
import org.command4spring.remote.http.receiver.JettyUnitServlet;
import org.command4spring.remote.http.receiver.TestHttpReceiverServlet;
import org.command4spring.result.Result;
import org.command4spring.testbase.test.AbstractDispatcherTest;
import org.command4spring.xml.serializer.XmlSerializer;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RestHttpDispatcherTest extends AbstractDispatcherTest {

    static Server server;

    final RestHttpDispatcher dispatcher;

    public RestHttpDispatcherTest() {
        this.dispatcher = new RestHttpDispatcher("http://localhost:8123/", new XmlSerializer());
        this.dispatcher.setTimeout(1000);
    }

    @Test(expected=CircuiteBreakerException.class)
    public void testCircuiteBreaker() throws DispatchException {
        //send to invalid port
        RestHttpDispatcher dispatcher = new RestHttpDispatcher("http://localhost:9999/", new XmlSerializer());
        CircuitBreakerFilter circuitBreakerFilter = new CircuitBreakerFilter();
        circuitBreakerFilter.setErrorThreshold(2);
        circuitBreakerFilter.setOpenInterval(500);
        dispatcher.addFilter(circuitBreakerFilter);
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        //after 2 failure CB should be in open state
        dispatcher.dispatch(new SampleCommand()).getResult();
    }

    private void dispatchWithoutError(final RestHttpDispatcher dispatcher, final Command<? extends Result> command) {
        try {
            dispatcher.dispatch(command).getResult(5000, TimeUnit.MILLISECONDS);
        } catch (DispatchException e) {
            System.out.println(e.getMessage());
        }
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
