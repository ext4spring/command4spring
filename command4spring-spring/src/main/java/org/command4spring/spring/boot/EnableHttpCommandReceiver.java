package org.command4spring.spring.boot;

import org.command4spring.spring.config.DefaultHttpReceiverConfiguration;
import org.command4spring.spring.remote.http.receiver.SpringCommandReceiverSerlvet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class EnableHttpCommandReceiver extends DefaultHttpReceiverConfiguration {

    @Bean
    public ServletRegistrationBean commandDispatcherServletRegistration() {
        return new ServletRegistrationBean(new SpringCommandReceiverSerlvet(), "/dispatcher");
    }

}
