package org.command4spring.remote.dispatcher.filter.circuit;

import org.junit.Assert;
import org.junit.Test;



public class CircuitBreakerFilterTest {

    @Test
    public void testCBChangesStates() throws InterruptedException {
        CircuitBreakerFilter cb=new CircuitBreakerFilter();
        cb.setErrorThreshold(2);
        cb.setOpenInterval(500);
        cb.setHalfOpenIntervall(500);
        //starts as closed
        Assert.assertTrue(cb.executionAllowed());
        Assert.assertEquals(CircuitBreakerState.CLOSED, cb.getState());

        //first error below threshold stays closed
        cb.bookError();
        Assert.assertTrue(cb.executionAllowed());
        Assert.assertEquals(CircuitBreakerState.CLOSED, cb.getState());

        //error reaches threshold, open
        cb.bookError();
        Assert.assertEquals(CircuitBreakerState.OPEN, cb.getState());
        Assert.assertFalse(cb.executionAllowed());

        //after open interval time it goes to half open
        Thread.sleep(600);
        Assert.assertTrue(cb.executionAllowed());
        Assert.assertEquals(CircuitBreakerState.HALF_OPEN, cb.getState());

        //the second call in half open are not enabled
        Assert.assertFalse(cb.executionAllowed());

        //after half open interval 1 request is allowed
        Thread.sleep(600);
        Assert.assertTrue(cb.executionAllowed());
        Assert.assertEquals(CircuitBreakerState.HALF_OPEN, cb.getState());
        Assert.assertFalse(cb.executionAllowed());

        //after some errors the first success request closes the circuite
        cb.bookError();
        cb.bookError();
        cb.bookSuccess();
        Assert.assertTrue(cb.executionAllowed());
        Assert.assertEquals(CircuitBreakerState.CLOSED, cb.getState());
    }
}
