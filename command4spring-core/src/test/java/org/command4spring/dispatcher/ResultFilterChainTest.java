package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.exception.DispatchException;
import org.command4spring.result.AbstractResult;
import org.command4spring.result.Result;
import org.junit.Assert;
import org.junit.Test;

public class ResultFilterChainTest {

    private class TestResult extends AbstractResult {
        String data = "";

        void append(final String text) {
            this.data += text;
        }

        public String getData() {
            return this.data;
        }
    }

    private class Filter1 implements ResultFilter {
        @Override
        public <R extends Result> R filter(final R result, final ResultFilterChain filterChain) throws DispatchException {
            if (result instanceof TestResult) {
                ((TestResult) result).append("1");
            }
            return filterChain.filter(result);
        }
    }

    private class Filter2 implements ResultFilter {
        @Override
        public <R extends Result> R filter(final R result, final ResultFilterChain filterChain) throws DispatchException {
            if (result instanceof TestResult) {
                ((TestResult) result).append("2");
            }
            return filterChain.filter(result);
        }
    }

    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
        List<ResultFilter> filters = Arrays.asList(new ResultFilter[] { new Filter1(), new Filter2() });
        DefaultResultFilterChain filterChain = new DefaultResultFilterChain(filters);
        TestResult result = new TestResult();
        TestResult filteredResult = filterChain.filter(result);
        Assert.assertEquals("12", filteredResult.getData());
    }

}
