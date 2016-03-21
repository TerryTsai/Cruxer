package email.com.gmail.ttsai0509.cruxer.rest;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.production.WebMvcConfig;
import email.com.gmail.ttsai0509.cruxer.model.Role;
import email.com.gmail.ttsai0509.cruxer.controller.rest.HoldRestController;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Hold;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.repository.HoldRepository;
import email.com.gmail.ttsai0509.cruxer.utils.AssertEx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class HoldControllerTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private HoldRepository holdRepository;
    @Autowired private HoldRestController holdRestController;

    private Account account;

    @Before
    public void before() {
        account = Account.createAccount("jdoe05", "abc123", "John", "Doe", "jdoe05@example.com", Role.USER);
        accountRepository.save(account);
    }

    @After
    public void after() {
        holdRepository.deleteAllInBatch();
        accountRepository.delete(account);
    }

    @Test
    public void authenticationEndpointsTest() {
        AssertEx.exception(
                AuthenticationCredentialsNotFoundException.class,
                () -> holdRestController.getHold(null),
                () -> holdRestController.postHold(null, null),
                () -> holdRestController.putHold(null, null),
                () -> holdRestController.getHolds(null)
        );
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void getHoldPass() {
        Hold expected = holdRepository.save(new Hold(account, "model.obj", ""));
        Hold actual = holdRestController.getHold(expected.getId());
        Assert.assertTrue(expected.matches(actual));
    }

}
