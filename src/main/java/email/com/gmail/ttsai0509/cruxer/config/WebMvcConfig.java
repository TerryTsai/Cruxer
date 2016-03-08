package email.com.gmail.ttsai0509.cruxer.config;

import email.com.gmail.ttsai0509.cruxer.intercept.AccountInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AccountInjector accountInjector;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("file:site/favicon.ico");
        registry.addResourceHandler("/css/**").addResourceLocations("file:site/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("file:site/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("file:site/js/");

        registry.addResourceHandler("/files/**").addResourceLocations("file:files/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accountInjector);

    }

}
