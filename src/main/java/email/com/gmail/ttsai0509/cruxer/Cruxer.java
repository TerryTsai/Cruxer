package email.com.gmail.ttsai0509.cruxer;

import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan
@SpringBootApplication
public class Cruxer {

    private static final Logger log = LoggerFactory.getLogger(Cruxer.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Cruxer.class);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/404.html")
        );
    }

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepo) {
        return username -> {
            Account account = accountRepo.findByUsername(username);
            if (account == null)
                throw new UsernameNotFoundException(username + " not found.");
            return account.toUser();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
