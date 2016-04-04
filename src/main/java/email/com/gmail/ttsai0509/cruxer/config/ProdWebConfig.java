package email.com.gmail.ttsai0509.cruxer.config;

import email.com.gmail.ttsai0509.cruxer.intercept.AccountInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@Profile("production")
public class ProdWebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AccountInjector accountInjector;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/web/favicon.ico");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/web/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/web/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/web/js/");
        registry.addResourceHandler("/jsx/**").addResourceLocations("classpath:/web/jsx/");

        // TODO : Server should be stateless. Files belong on separate file server.
        // registry.addResourceHandler("/files/**").addResourceLocations("file:files/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accountInjector);

    }

}
