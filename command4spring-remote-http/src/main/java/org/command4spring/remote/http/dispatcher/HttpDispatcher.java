package org.command4spring.remote.http.dispatcher;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.RemoteDispatcher;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.remote.http.mapper.HttpMapper;
import org.command4spring.result.Result;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 */
public class HttpDispatcher extends AbstractDispatcher implements RemoteDispatcher {

    private static final Log LOGGER = LogFactory.getLog(HttpDispatcher.class);
    
    private final CloseableHttpClient httpclient;
    private final HttpMapper httpMapper;
    private final String targetUrl;

    public HttpDispatcher(final HttpMapper httpMapper, final String targetUrl) {
	this(HttpClients.createDefault(), httpMapper, targetUrl);
    }

    public HttpDispatcher(final CloseableHttpClient httpclient, HttpMapper httpMapper, final String targetUrl) {
	super();
	this.httpclient = httpclient;
	this.httpMapper = httpMapper;
	this.targetUrl = targetUrl;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException {
	try {	    
	    HttpRequestBase httpRequest=this.httpMapper.createRequest(command, this.targetUrl);
	    LOGGER.debug("Sending HTTP request to:"+httpRequest.getURI().toString()+" Method:"+httpRequest.getMethod());
	    CloseableHttpResponse response = this.httpclient.execute(httpRequest);
	    return (R) this.httpMapper.parseResponse(response);
	} catch (ClientProtocolException e) {
	    throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + this.targetUrl + ". Error message:" + e, e);
	} catch (IOException e) {
	    throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + this.targetUrl + ". Error message:" + e, e);
	}
    }

}
