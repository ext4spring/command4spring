package org.command4spring.service;

import javax.jws.WebService;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service
@WebService(endpointInterface = "org.command4spring.service.JaxwsDispatcherService")
public class DefaultJaxwsDispatcherService extends SpringBeanAutowiringSupport implements JaxwsDispatcherService {

    private final DispatcherService dispatcherService;

    @Autowired
    public DefaultJaxwsDispatcherService(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public <C extends Command<R>, R extends Result> R dispatch(C command) throws DispatchException {
        return this.dispatcherService.dispatch(command);
    }

}
