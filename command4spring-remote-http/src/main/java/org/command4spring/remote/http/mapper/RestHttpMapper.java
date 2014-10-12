package org.command4spring.remote.http.mapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;

public class RestHttpMapper implements HttpMapper {
    private final Serializer serializer;
    public static final String COMMAND_ID_HEADER = "commandId";
    public static final String COMMAND_CLASS_HEADER = "commandClass";
    public static final String RESULT_CLASS_HEADER = "resultClass";

    private static final Log LOGGER = LogFactory.getLog(RestHttpMapper.class);

    public RestHttpMapper(Serializer serializer) {
	super();
	this.serializer = serializer;
    }

    @Override
    public HttpRequestBase createRequest(Command<? extends Result> command, String targetUrl) throws DispatchException {
	HttpRequestBase request;
	String requestPath = this.createHttpPath(command, targetUrl);
	try {
	    request = new HttpPost(requestPath);
	    String body = this.serializer.toText(command);
	    ((HttpPost) request).setEntity(new StringEntity(body));
	    request.setHeader(COMMAND_CLASS_HEADER, command.getCommandType().getName());
	    request.setHeader(COMMAND_ID_HEADER, command.getCommandId());
	    return request;
	} catch (UnsupportedEncodingException e) {
	    throw new DispatchException("Error while creating HTTP message from command:" + e, e);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public Command<? extends Result> parseRequest(HttpServletRequest httpRequest) throws DispatchException {
	try {
	    Command<? extends Result> command;
	    if (httpRequest.getMethod().equals("GET")) {
		String commandClass = httpRequest.getHeader(COMMAND_CLASS_HEADER);
		command = (Command<? extends Result>) Class.forName(commandClass).newInstance();
	    } else {
		String textCommand = IOUtils.toString(httpRequest.getInputStream());
		command = this.serializer.toCommand(textCommand);
	    }

	    return command;
	} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
	    throw new DispatchException("Error while convert http request into Command object:" + e, e);
	}
    }

    @Override
    public void writeResponse(Result result, HttpServletResponse httpResponse) throws DispatchException {
	try {
	    String textResult = serializer.toText(result);
	    httpResponse.setHeader(RESULT_CLASS_HEADER, result.getClass().getName());
	    httpResponse.setHeader(COMMAND_ID_HEADER, result.getCommandId());
	    IOUtils.write(textResult, httpResponse.getOutputStream());
	} catch (IOException e) {
	    this.writeError(e, httpResponse);
	}
    }

    public void writeError(Throwable throwable, HttpServletResponse httpResponse) {
	try {
	    // TODO: proper error response
	    IOUtils.write(throwable.getMessage(), httpResponse.getOutputStream());
	} catch (IOException e) {
	    LOGGER.error("Error while writing error to response:" + throwable.getMessage(), throwable);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Result> R parseResponse(CloseableHttpResponse httpResponse) throws DispatchException {
	int status = httpResponse.getStatusLine().getStatusCode();
	HttpEntity entity = httpResponse.getEntity();
	String responseBody;
	if (entity != null) {
	    try {
		responseBody = EntityUtils.toString(entity);
	    } catch (ParseException | IOException e) {
		throw new RemoteDispatchException("Error reading HTTP response. HTTP status:" + status + "." + e, e);
	    }
	} else {
	    // TODO: is this case possible?
	    responseBody = "";
	}
	if (status == HttpStatus.SC_OK) {
	    return (R) this.serializer.toResult(responseBody);
	} else {
	    throw new RemoteDispatchException("Error response received. HTTP status:" + status + " Message:" + responseBody);
	}
    }

    protected String createHttpPath(final Command<? extends Result> command, String targetUrl) throws DispatchException {
	Path pathAnnotation = command.getClass().getAnnotation(Path.class);
	if (pathAnnotation != null) {
	    String pathTemplate = pathAnnotation.value();
	    if (pathTemplate.contains("{")) {
		try {
		    for (Field field : command.getClass().getDeclaredFields()) {
			if (pathTemplate.contains("{" + field.getName() + "}")) {
			    field.setAccessible(true);
			    pathTemplate = pathTemplate.replaceAll("\\{" + field.getName() + "}", field.get(command).toString());
			    field.setAccessible(false);
			}
		    }
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
		    throw new DispatchException("Error while resolving http request path:" + e, e);
		}
	    }
	    return targetUrl + pathTemplate;
	} else {
	    return targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
	}
    }

    public static void main(String[] args) {
	String pathTemplate = "/something/{time}";
	System.out.println(pathTemplate.contains("{"));
	System.out.println(pathTemplate.contains("{time}"));
	System.out.println(pathTemplate.replaceAll("\\{time}", "4"));
    }
}
