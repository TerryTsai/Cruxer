package email.com.gmail.ttsai0509.cruxer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService;

    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/favicon.ico", "css/**", "/images/**", "/img/**", "/js/**", "/lib/**").permitAll()
                .antMatchers("/files/**").hasRole("USER")
                .anyRequest().permitAll()
                .and().formLogin()
                        .loginPage("/login.html")
                        .failureUrl("/login.html?error")
                        .defaultSuccessUrl("/")
                        .permitAll()
                .and().logout()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll()
                .and().httpBasic();
    }

}
