package org.command4spring.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Validation exception that contains the error message, the reasons why it failed 
 */
public class VadilationException extends DispatchException {

    private static final long serialVersionUID = 1L;
    private final List<String> reasons;
    
    public VadilationException(String message, String... reasons) {
        super(message);
        if (reasons!=null) {
            this.reasons=Arrays.asList(reasons);
        } else {
            this.reasons=new ArrayList<String>();
        }
    }
    
    public List<String> getReasons() {
        return reasons;
    }


}
