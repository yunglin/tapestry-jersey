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

package com.bluetangstudio.shared.jersey.services;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class JerseyModule {

    /**
     * Contribute the default value of {@link JerseySymbols.REQUEST_PATH_PREFIX} to Tapestry.
     * 
     * @param configuration
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(JerseySymbols.REQUEST_PATH_PREFIX, "/rest");
    }

    /**
     * Added {@link JerseyRequestFilter} to the very beginning of servlet filter chain.
     * 
     * @param configuration
     * @param jerseyFilter
     */
    public void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration,
            @Service("JerseyHttpServletRequestFilter") HttpServletRequestFilter jerseyFilter) {
        configuration.add("JerseyFilter", jerseyFilter, "after:StoreIntoGlobals", "before:EndOfRequest", "before:GZIP");
    }

    /**
     * 
     * @param configurations
     * @return
     */
    @Scope(ScopeConstants.DEFAULT)
    @EagerLoad
    public static Application buildJerseyRootResources(Collection<Object> configurations) {
        return new TapestryEnabledApplication(configurations);
    }

    @Scope(ScopeConstants.DEFAULT)
    @EagerLoad
    public static HttpServletRequestFilter buildJerseyHttpServletRequestFilter(
            @Service("JerseyRootResources") Application jaxwsApplication, ApplicationGlobals applicationGlobal,
            ObjectLocator objectLocator) throws ServletException {

        ServletContainer jaxwsContainer = new ServletContainer(jaxwsApplication);
        final ServletContext servletContext = applicationGlobal.getServletContext();
        final Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("javax.ws.rs.Application", TapestryEnabledApplication.class.getName());

        jaxwsContainer.init(new FilterConfig() {

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            public Enumeration<?> getInitParameterNames() {
                return params.elements();
            }

            @Override
            public String getInitParameter(String name) {
                return params.get(name);
            }

            @Override
            public String getFilterName() {
                return "JerseyHttpServletRequestFilter";
            }
        });
        JerseyRequestFilter ret = objectLocator.autobuild(JerseyRequestFilter.class);
        ret.setServletContainer(jaxwsContainer);
        return ret;
    }

}
