package org.command4spring.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

@WebService(name="dispatcher")
public interface JaxwsDispatcherService {

    @WebMethod
    public <C extends Command<R>, R extends Result> R dispatch(C command) throws DispatchException;
}
