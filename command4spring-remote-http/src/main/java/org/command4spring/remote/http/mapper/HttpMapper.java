package org.command4spring.remote.http.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.Result;

public interface HttpMapper {
    /**
     * Create HTTP request from the {@link Command}
      * @throws DispatchException
     */
    HttpRequestBase createRequest(final Command<? extends Result> command, String targetUrl) throws DispatchException;
    
    /**
     * Parse HTTP response into {@link Result}
     * @throws DispatchException
     */
    <R extends Result> R parseResponse(final CloseableHttpResponse httpResponse) throws DispatchException;

    /**
     * Parse HTTP request created by createRequest method into {@link Command}
     * @param httpRequest
     * @return
     * @throws RemoteDispatchException
     */
    Command<? extends Result> parseRequest(final HttpServletRequest httpRequest) throws DispatchException;
    
    /**
     * 
     * @param httpResponse
     * @throws DispatchException
     */
    void writeResponse(Result result, HttpServletResponse httpResponse) throws DispatchException;
    
    public void writeError(Throwable throwable, HttpServletResponse httpResponse);
    
}
