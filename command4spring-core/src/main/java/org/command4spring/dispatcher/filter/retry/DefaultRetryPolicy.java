package org.command4spring.dispatcher.filter.retry;
/**
 * Default policy for retry filter. By default it doubles the wait time before each retry and max retry count is 3 
 * @author pborbas
 *
 */
public class DefaultRetryPolicy implements RetryPolicy {

    private int maxRetryCount = 3;
    private long baseWaitTime = 1000;
    private float multiplier = 2f;

    @Override
    public int getMaxRetryCount() {
        return this.maxRetryCount;
    }

    @Override
    public long getWaitTime(final int retryCount) {
        long waitTime = this.baseWaitTime;
        for (int i = 0; i < retryCount; i++) {
            waitTime = Math.round(waitTime * this.multiplier);
        }
        return waitTime;
    }

    public void setMaxRetryCount(final int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * Wait time before the first retry
     */
    public void setBaseWaitTime(final long baseWaitTime) {
        this.baseWaitTime = baseWaitTime;
    }

    /**
     * Multiplier of the waittime for each retry
     */
    public void setMultiplier(final float multiplier) {
        this.multiplier = multiplier;
    }

}
