package org.command4spring.remote.http.dispatcher;

import java.io.IOException;
import java.util.concurrent.Future;

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
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.dispatcher.RemoteDispatcher;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
import org.command4spring.service.DispatcherService;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 * Synchronous (in VM) implementation of the {@link DispatcherService}
 */
@Service
public class HttpDispatcher implements RemoteDispatcher {

    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private final Serializer serializer;
    private final String targetUrl;

    public HttpDispatcher(CloseableHttpClient httpclient, Serializer serializer, String targetUrl) {
        super();
        this.httpclient = httpclient;
        this.serializer = serializer;
        this.targetUrl = targetUrl;
    }

    @Override
    public <C extends Command<R>, R extends Result> Future<R> dispatch(C command) throws DispatchException {
        try {
            HttpPost httpPost = new HttpPost(targetUrl);
            String body = this.serializer.toText(command);
            httpPost.setEntity(new StringEntity(body));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String responseBody;
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            } else {
                responseBody = "";
            }
            if (status == HttpStatus.SC_OK) {
                return new AsyncResult<R>((R) serializer.toResult(responseBody));
            } else {
                throw new RemoteDispatchException("Error response received from URL:" + targetUrl + ". HTTP status:" + status + " Message:" + responseBody);
            }
        } catch (ClientProtocolException e) {
            throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + targetUrl + ". Error message:" + e, e);
        } catch (IOException e) {
            throw new RemoteDispatchException("Error while sending command through HTTP to URL:" + targetUrl + ". Error message:" + e, e);
        }
    }

}
