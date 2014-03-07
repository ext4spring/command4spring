package org.command4spring.result;



import org.junit.Assert;
import org.junit.Test;

public class PredefinedResultsTest {

    @Test
    public void testBooleanResult() {
        BooleanResult result=new BooleanResult("1", false);
        Assert.assertEquals(false, result.getValue());
        result.setValue(true);
        Assert.assertEquals(true, result.getValue());
    }

    @Test
    public void testLongResult() {
        LongResult result=new LongResult("1", 5l);
        Assert.assertEquals(new Long(5l), result.getValue());
        result.setValue(6l);
        Assert.assertEquals(new Long(6l), result.getValue());
    }
    
}
