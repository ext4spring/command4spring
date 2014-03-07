package org.command4spring.jms.result;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.result.Result;

//TODO: remove old results

public class ResultMessageListener implements MessageListener {

    private static final Log LOGGER = LogFactory.getLog(ResultMessageListener.class);
    private final Map<String, Long> timeouts = new HashMap<String, Long>();
    private final Map<String, BlockingQueue<Result>> results = new HashMap<String, BlockingQueue<Result>>();

    private long reponseTimeout=30000;
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage resultMessage = (ObjectMessage) message;
        try {
            Result result = (Result) resultMessage.getObject();
            this.storeResult(result);
            LOGGER.debug("Response received:" + result);
        } catch (JMSException e) {
            LOGGER.error("Error while receiving response:" + e, e);
        } catch (InterruptedException e) {
            LOGGER.error("Error while receiving response:" + e, e);
        }
    }

    public JmsFutureResult<Result> handle(String commandId) {
        this.timeouts.put(commandId, System.currentTimeMillis());
        this.results.put(commandId, new ArrayBlockingQueue<Result>(1));
        this.clearTimeOutedResults();
        return new JmsFutureResult<Result>(commandId, this);
    }

    private void clearTimeOutedResults() {
        long currentTime=System.currentTimeMillis();
        for (String commandId:this.timeouts.keySet()) {
            long startTime=timeouts.get(commandId);
            if (currentTime-startTime>this.reponseTimeout) {
                this.remove(commandId);
            }
        }
    }
    
    protected void remove(String commandId) {
        this.results.remove(commandId);
        this.timeouts.remove(commandId);
    }

    protected boolean isHandlerFor(String commandId) {
        return this.results.containsKey(commandId);
    }

    protected boolean hasResultFor(String commandId) {
        return this.results.get(commandId).size() > 0;
    }

    protected Result waitForResult(String commandId) {
        Result result=this.results.get(commandId).poll();
        this.remove(commandId);
        return result;
    }

    protected Result waitForResult(String commandId, long timeout, TimeUnit unit) throws InterruptedException {
        Result result=this.results.get(commandId).poll(timeout, unit);
        if (result!=null) {
            this.remove(commandId);
        }
        return result;
    }

    private void storeResult(Result result) throws InterruptedException {
        this.results.get(result.getCommandId()).put(result);
    }

    /**
     * If the caller doesnt get the result in responseTimeout msec, the result gets dropped 
     * @param reponseTimeout
     */
    public void setReponseTimeout(long reponseTimeout) {
        this.reponseTimeout = reponseTimeout;
    }

}
