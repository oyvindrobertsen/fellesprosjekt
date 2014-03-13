package no.ntnu.apotychia.config;


import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.ServletContext;

public class ApotychiaSecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected String getDispatcherWebApplicationContextSuffix() {
        return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
    }

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        insertFilters(servletContext,
                new HiddenHttpMethodFilter(),
                new MultipartFilter());
    }

    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
