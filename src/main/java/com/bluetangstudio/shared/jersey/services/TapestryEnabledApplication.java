package com.bluetangstudio.shared.jersey.services;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Application that offers tapestry managed services to Jersey Application.
 *
 */
public class TapestryEnabledApplication extends Application {

    private Set<Object> _singletons;
    
    /**
     * 
     * @param singletons collection of Tapestry managed services and/or any thread-safe services.
     * Since Tapestry managed per-thread services are thread-safe services, you can add them to this
     * collection too.
     *
     */
    public TapestryEnabledApplication(Collection<?> singletons) {
        _singletons = Collections.unmodifiableSet(new HashSet<Object>(singletons));
    }
    
    public Set<Object> getSingletons() {
        return _singletons;
    }
        
}
