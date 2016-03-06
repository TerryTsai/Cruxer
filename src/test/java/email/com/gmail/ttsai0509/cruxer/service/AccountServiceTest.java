package email.com.gmail.ttsai0509.cruxer.service;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.WebMvcConfig;
import email.com.gmail.ttsai0509.cruxer.model.Role;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class AccountServiceTest {

    @Autowired private AccountService accountService;
    @Autowired private AccountRepository accountRepository;

    private Account account;

    @Before
    public void before() {
        account = Account.createAccount("jdoe05", "abc123", "John", "Doe", "jdoe05@example.com", Role.USER);
        accountRepository.save(account);
    }

    @After
    public void after() {
        accountRepository.delete(account);
    }

    @Test
    @WithMockUser(username = "jdoe05", roles = {"USER"})
    public void getCurrentAccount() {
        Account current = accountService.getCurrentAccount();
        Assert.assertEquals(account.getUsername(), current.getUsername());
        Assert.assertEquals(account.getHashword(), current.getHashword());
        Assert.assertEquals(account.getFirstname(), current.getFirstname());
        Assert.assertEquals(account.getLastname(), current.getLastname());
        Assert.assertEquals(account.getEmail(), current.getEmail());
    }

}
