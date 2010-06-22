package com.bluetangstudio.shared.jersey.services;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * HttpServletRequestFilter that passes requests with a predefined prefix 
 * (see {@link JerseySymbols#REQUEST_PATH_PREFIX) to Jersey container.
 * 
 * @author hyl
 */
public class JerseyRequestFilter implements HttpServletRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyRequestFilter.class);

    private static final FilterChain END_OF_CHAIN = new EndOfChainFilerChain();

    private ServletContainer _jaxwsContainer;

    private String _pathPrefix;

    public JerseyRequestFilter(String pathPrefix, ServletContainer container) {
        _pathPrefix = pathPrefix;
        _jaxwsContainer = container;

    }

    @Override
    public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
            throws IOException {

        if (!request.getRequestURI().startsWith(_pathPrefix)) {
            return handler.service(request, response);
        }

        try {
            _jaxwsContainer.doFilter(request, response, END_OF_CHAIN);
            return true;
        }
        catch (ServletException e) {
            LOG.info("{}", e);
            return false;
        }
    }

    private static final class EndOfChainFilerChain implements FilterChain {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        }
    }

}
