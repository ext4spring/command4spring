package org.command4spring.remote.http.dispatcher;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.AbstractDispatcher;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.RemoteDispatcher;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 */
public class HttpDispatcher extends AbstractDispatcher implements RemoteDispatcher {

    private final CloseableHttpClient httpclient;
    private final Serializer serializer;
    private final String targetUrl;

    public HttpDispatcher(final Serializer serializer, final String targetUrl) {
        this(HttpClients.createDefault(), serializer, targetUrl);
    }
    public HttpDispatcher(final CloseableHttpClient httpclient, final Serializer serializer, final String targetUrl) {
        super();
        this.httpclient = httpclient;
        this.serializer = serializer;
        this.targetUrl = targetUrl;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException {
        try {
            HttpPost httpPost = new HttpPost(this.targetUrl);
            String body = this.serializer.toText(command);
            httpPost.setEntity(new StringEntity(body));
            CloseableHttpResponse response = this.httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String responseBody;
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            } else {
                responseBody = "";
            }
            if (status == HttpStatus.SC_OK) {
                return (R) this.serializer.toResult(responseBody);
            } else {
                throw new RemoteDispatchException("Error response received from URL:" + this.targetUrl + ". HTTP status:" + status + " Message:" + responseBody);
            }
        } catch (ClientProtocolException e) {
            throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + this.targetUrl + ". Error message:" + e, e);
        } catch (IOException e) {
            throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + this.targetUrl + ". Error message:" + e, e);
        }
    }
}
