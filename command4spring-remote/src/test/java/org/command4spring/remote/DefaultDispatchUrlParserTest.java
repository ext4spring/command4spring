package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;
import org.junit.Assert;
import org.junit.Test;

public class DefaultDispatchUrlParserTest {

    @Test
    public void testValidHttpUrl() throws DispatchUrlParsingException {
	String url = "http://name:xml@hostname:8080/?timeout=5";
	DefaultDispatchUrlParser parser = new DefaultDispatchUrlParser();
	DispatcherUrl dispatcherUrl = parser.parse(url);
	Assert.assertEquals("http", dispatcherUrl.getProtocol());
	Assert.assertEquals("hostname:8080", dispatcherUrl.getHostAndPort());
	Assert.assertEquals("name", dispatcherUrl.getName());
	Assert.assertEquals("xml", dispatcherUrl.getSerializer());
	Assert.assertEquals(1, dispatcherUrl.getParameters().size());
	Assert.assertEquals(5, dispatcherUrl.getTimeout());
    }

    @Test
    public void testValidJmsUrl() throws DispatchUrlParsingException {
	String url = "jms://name:xml@hostname:61616/?timeout=5&commandQueue=c&resultQueue=r";
	DefaultDispatchUrlParser parser = new DefaultDispatchUrlParser();
	DispatcherUrl dispatcherUrl = parser.parse(url);
	Assert.assertEquals("jms", dispatcherUrl.getProtocol());
	Assert.assertEquals("hostname:61616", dispatcherUrl.getHostAndPort());
	Assert.assertEquals("name", dispatcherUrl.getName());
	Assert.assertEquals("xml", dispatcherUrl.getSerializer());
	Assert.assertEquals(3, dispatcherUrl.getParameters().size());
	Assert.assertEquals(5, dispatcherUrl.getTimeout());
	Assert.assertEquals("c", dispatcherUrl.getParameters().get("commandQueue"));
	Assert.assertEquals("r", dispatcherUrl.getParameters().get("resultQueue"));
    }

    @Test
    public void invalidUrlsThrowsHandledExceptions() {
	DefaultDispatchUrlParser parser = new DefaultDispatchUrlParser();
	String[] invalids = new String[] { "http://name:xml@hostname:8080/?timeout=", "http://name:xml@hostname:8080/?asd=5", "http://name:xml@hostname:8080?timeout=5",
		"http://name@hostname:8080/?timeout=5", "http://hostname:8080/?timeout=5", "name:xml@hostname:8080/?timeout=5" };
	for (String invalidUrl : invalids) {
	    try {
		parser.parse(invalidUrl);
	    } catch (DispatchUrlParsingException e) {
		// ok
	    }
	}
    }
}
