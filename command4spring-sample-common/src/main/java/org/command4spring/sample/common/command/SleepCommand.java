package org.command4spring.sample.common.command;

import javax.ws.rs.Path;

import org.command4spring.command.AbstractCommand;
import org.command4spring.result.VoidResult;

@Path("/sleep/{time}")
public class SleepCommand extends AbstractCommand<VoidResult>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    final long time;

    public SleepCommand(long time) {
	super();
	this.time = time;
    }
   
    public long getTime() {
	return time;
    }
    
}
