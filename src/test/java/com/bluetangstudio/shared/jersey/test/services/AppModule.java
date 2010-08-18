// Copyright 2007, 2008, 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.bluetangstudio.shared.jersey.test.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.bluetangstudio.shared.jersey.services.JerseyModule;
import com.bluetangstudio.shared.jersey.test.services.rest.HelloWorld;
import com.bluetangstudio.shared.jersey.test.services.rest.HelloWorldImpl;

@SubModule(JerseyModule.class)
public class AppModule {
    
    public static void bind(ServiceBinder binder) {
        binder.bind(HelloWorld.class, HelloWorldImpl.class);
    }
    
    public static void contributeJerseyRootResources(Configuration<Object> configuration, ObjectLocator objectLocator) {
        configuration.add(objectLocator.getService(HelloWorld.class));
    }

}
