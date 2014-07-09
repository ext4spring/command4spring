package org.command4spring.example;

import org.command4spring.command.AbstractCommand;

public class SampleCommand extends AbstractCommand<SampleResult> {

    private static final long serialVersionUID = -8164946958544815277L;
    private String data;
    
    public SampleCommand() {
    }
    
    
    public SampleCommand(String data) {
        super();
        this.data = data;
    }


    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }
}
