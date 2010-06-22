package com.bluetangstudio.shared.jersey.test.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.bluetangstudio.shared.jersey.services.JerseyModule;
import com.bluetangstudio.shared.jersey.test.services.rest.HelloWorld;
import com.bluetangstudio.shared.jersey.test.services.rest.HelloWorldResourceImpl;

@SubModule(JerseyModule.class)
public class AppModule {
    
    public static void bind(ServiceBinder binder) {
        binder.bind(HelloWorld.class, HelloWorldResourceImpl.class);
    }
    
    public static void contributeApplication(Configuration<Object> configuration, ObjectLocator objectLocator) {
        configuration.add(objectLocator.getService(HelloWorld.class));
    }

}
