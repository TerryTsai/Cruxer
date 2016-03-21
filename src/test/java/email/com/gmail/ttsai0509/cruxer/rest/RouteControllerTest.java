package email.com.gmail.ttsai0509.cruxer.rest;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.production.WebMvcConfig;
import email.com.gmail.ttsai0509.cruxer.controller.rest.RouteRestController;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Role;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.repository.RouteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class RouteControllerTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private RouteRepository routeRepository;
    @Autowired private RouteRestController routeRestController;

    private Account account;

    @Before
    public void before() {
        account = Account.createAccount("jdoe05", "abc123", "John", "Doe", "jdoe05@example.com", Role.USER);
        accountRepository.save(account);
    }

    @After
    public void after() {
        routeRepository.deleteAllInBatch();
        accountRepository.delete(account);
    }

}
