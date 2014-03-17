package no.ntnu.apotychia.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * Maps all Emberjs routes to index so that they work with direct linking.
     */
    @Controller
    static class Routes {

        @RequestMapping({
                "/",
                "/me"
        })
        public String index() {
            return "/index.html";
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/register").setViewName("register.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}