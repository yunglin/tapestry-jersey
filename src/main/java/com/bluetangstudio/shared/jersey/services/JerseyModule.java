package com.bluetangstudio.shared.jersey.services;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.HttpServletRequestFilter;

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
     * @param configuration
     * @param jerseyFilter
     */
    public void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration,
            @Service("JerseyHttpServletRequestFilter") HttpServletRequestFilter jerseyFilter) {
        configuration.add("JerseyFilter", jerseyFilter, "before:GZIP");
    }

    /**
     * 
     * @param configurations
     * @return
     */
    @Scope(ScopeConstants.DEFAULT)
    @EagerLoad
    public static Application buildApplication(Collection<Object> configurations) {
        return new TapestryEnabledApplication(configurations);
    }

    @Scope(ScopeConstants.DEFAULT)
    @EagerLoad
    public static HttpServletRequestFilter buildJerseyHttpServletRequestFilter(
            @Inject @Symbol(JerseySymbols.REQUEST_PATH_PREFIX) String pathPrefix,
            Application jaxwsApplication,
            ApplicationGlobals applicationGlobal) throws ServletException {
        
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
        return new JerseyRequestFilter(pathPrefix, jaxwsContainer);
    }

}