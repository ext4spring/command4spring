package org.command4spring.spring.remote.http.receiver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.command4spring.remote.http.receiver.CommandReceiverServlet;
import org.command4spring.remote.receiver.CommandReceiver;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class SpringCommandReceiverSerlvet extends CommandReceiverServlet {

    private static final String COMMAND_RECEIVER_INIT_PARAMETER="command_receiver";

    @Override
    protected CommandReceiver getCommandReceiver(final ServletConfig config) throws ServletException {
        CommandReceiver commandReceiver;
        String beanName=config.getInitParameter(COMMAND_RECEIVER_ATTRIBUTE);
        if (beanName==null) {
            commandReceiver=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(CommandReceiver.class);
        } else {
            commandReceiver=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, CommandReceiver.class);                      
        }
        return commandReceiver;
    }


}
