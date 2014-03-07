package org.command4spring.result;

import org.junit.Test;

public class AbstractResultTest {

    private class TestResult extends AbstractResult {

        public TestResult(String commandId) {
            super(commandId);
        }
        
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResultMustHaveCommandId() {
        new TestResult(null);
    }
   
    
}
