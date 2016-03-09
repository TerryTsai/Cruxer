package email.com.gmail.ttsai0509.cruxer.config.development;

import email.com.gmail.ttsai0509.cruxer.intercept.AccountInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Profile("development")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AccountInjector accountInjector;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("file:src/main/resources/web/favicon.ico");
        registry.addResourceHandler("/css/**").addResourceLocations("file:src/main/resources/web/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("file:src/main/resources/web/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("file:src/main/resources/web/js/");

        registry.addResourceHandler("/files/**").addResourceLocations("file:files/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accountInjector);

    }

}
