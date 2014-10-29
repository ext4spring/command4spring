package org.command4spring.dispatcher.filter.retry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.dispatcher.filter.DispatchFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;

public class RetryFilter implements DispatchFilter {
    private static final Log LOG = LogFactory.getLog(RetryFilter.class);
    private final RetryPolicy retryPolicy;

    public RetryFilter() {
        this.retryPolicy = new DefaultRetryPolicy();
    }

    public RetryFilter(final RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    @Override
    public DispatchResult<? extends Result> filter(final DispatchCommand dispatchCommand, final DispatchFilterChain filterChain) throws DispatchException {
        try {
            return filterChain.filter(dispatchCommand);
        } catch (Throwable e) {
            return this.doRetry(0, dispatchCommand, filterChain, e);
        }
    }

    protected DispatchResult<? extends Result> doRetry(int retryCount, final DispatchCommand dispatchCommand, final DispatchFilterChain filterChain, final Throwable lastException)
            throws DispatchException {
        retryCount++;
        if (retryCount <= this.retryPolicy.getMaxRetryCount()) {
            long waitTime=this.retryPolicy.getWaitTime(retryCount);
            LOG.error("Error while executing command, retrying after "+waitTime+" msec. Error: " + lastException );
            try {
                Thread.sleep(waitTime);
                LOG.debug("Retrying command. Retry:" + retryCount + "/" + this.retryPolicy.getMaxRetryCount());
                return filterChain.filter(dispatchCommand);
            } catch (Throwable e) {
                return this.doRetry(retryCount, dispatchCommand, filterChain, e);
            }
        }
        LOG.error("All retries failed");
        if (lastException instanceof DispatchException) {
            throw (DispatchException) lastException;
        }
        throw new DispatchException("All retries failed.", lastException);
    }

}
