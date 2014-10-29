package org.command4spring.dispatcher.filter.retry;

public interface RetryPolicy {
    int getMaxRetryCount();
    long getWaitTime(int retryCount);
}       
