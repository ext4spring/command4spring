package org.command4spring.dispatcher.filter.retry;

import org.junit.Assert;
import org.junit.Test;

public class DefaultRetryPolicyTest {

    @Test
    public void testWaitTimeIsMultipliedForEachRetry() {
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy();
        retryPolicy.setBaseWaitTime(1000);
        retryPolicy.setMaxRetryCount(3);
        retryPolicy.setMultiplier(2);
        Assert.assertEquals(1000, retryPolicy.getWaitTime(0));
        Assert.assertEquals(2000, retryPolicy.getWaitTime(1));
        Assert.assertEquals(4000, retryPolicy.getWaitTime(2));
        Assert.assertEquals(8000, retryPolicy.getWaitTime(3));
    }

}
