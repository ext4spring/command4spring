package org.command4spring.remote.http.receiver;

//
//========================================================================
//Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.example.SampleAction;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.remote.receiver.CommandReceiver;
import org.command4spring.remote.receiver.DefaultCommandReceiver;
import org.command4spring.serializer.Serializer;
import org.command4spring.xml.serializer.XmlSerializer;

@SuppressWarnings("serial")
public class TestHttpReceiverServlet extends AbstractHttpCommandReceiverServlet {

    @Override
    protected CommandReceiver initCommandReceiver(final ServletConfig config) throws ServletException {
        try {
            Serializer serializer = new XmlSerializer();
            InVmDispatcher dispatcher = new InVmDispatcher();
            dispatcher.registerAction(new SampleAction());
            DefaultCommandReceiver commandReceiver = new DefaultCommandReceiver(serializer, dispatcher);
            return commandReceiver;
        } catch (DuplicateActionException e) {
            throw new ServletException(e);
        }
    }

}
