package email.com.gmail.ttsai0509.cruxer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// TODO : To be removed
//@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Cruxer")
                        .description("Cruxer API")
                        .contact("Terry Tsai")
                        .license("Apache License Version 2.0")
                        .version("0.1")
                        .build())
                .select()
                .paths(input -> input.startsWith("/accounts")
                        || input.startsWith("/comments")
                        || input.startsWith("/ratings")
                        || input.startsWith("/holds")
                        || input.startsWith("/walls")
                        || input.startsWith("/routes"))
                .build();
    }

}
