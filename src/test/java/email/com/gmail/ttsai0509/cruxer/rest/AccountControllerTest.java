package email.com.gmail.ttsai0509.cruxer.rest;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.WebMvcConfig;
import email.com.gmail.ttsai0509.cruxer.model.Role;
import email.com.gmail.ttsai0509.cruxer.controller.rest.AccountRestController;
import email.com.gmail.ttsai0509.cruxer.exception.CruxerException;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.utils.AssertEx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class AccountControllerTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountRestController accountRestController;

    private Account account1;
    private Account account2;

    @Before
    public void before() {
        account1 = Account.createAccount("jdoe05", "abc123", "John", "Doe", "jdoe05@example.com", Role.USER);
        accountRepository.save(account1);
        account2 = Account.createAccount("one", "two", "three", "four", "five@example.com", Role.USER);
        accountRepository.save(account2);
    }

    @After
    public void after() {
        accountRepository.delete(account1);
        accountRepository.delete(account2);
    }

    @Test
    public void postAccount() {
        Account expected = accountRestController.postAccount("user", "user", "Guest", "User", "user@guest.com");
        Account actual = accountRepository.findByUsername("user");
        Assert.assertTrue(expected.matches(actual));
    }

    @Test
    public void postAccountEmailExists() {
        AssertEx.exception(
                CruxerException.class,
                () -> accountRestController.postAccount("test", "test", "test", "test", "jdoe05@example.dom")
        );
    }

    @Test
    public void postAccountUserExists() {
        AssertEx.exception(
                CruxerException.class,
                () -> accountRestController.postAccount("jdoe05", "test", "test", "test", "user@guest.com")
        );
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void getAccount() {
        Account expected = account1;
        Account actual = accountRestController.getAccount(account1.getId());
        Assert.assertTrue(expected.matches(actual));
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void putAccountPass() {
        Account expected = accountRestController.putAccount(account1.getId(), "un", "hw", "fn", "ln", "em");
        Account actual = accountRepository.findOne(account1.getId());
        Assert.assertTrue(expected.matches(actual));

        expected = Account.createAccount("un", "hw", "fn", "ln", "em");
        expected.setId(account1.getId());
        Assert.assertTrue(expected.matches(actual));
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void putAccountUpdateOneField() {
        Account expected = accountRestController.putAccount(account1.getId(), null, null, "fn", null, null);
        Account actual = accountRepository.findOne(account1.getId());
        Assert.assertTrue(expected.matches(actual));

        expected = Account.createAccount("jdoe05", "abc123", "fn", "Doe", "jdoe05@example.com");
        expected.setId(account1.getId());
        Assert.assertTrue(expected.matches(actual));
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void putAccountAccessDenied() {
        AssertEx.exception(
                AccessDeniedException.class,
                () -> accountRestController.putAccount(account2.getId(), "1", "2", "3", "4", "5")
        );
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void putAccountUserExists() {
        AssertEx.exception(
                CruxerException.class,
                () -> accountRestController.putAccount(account1.getId(), "one", "2", "3", "4", "5")
        );
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void putAccountEmailExists() {
        Account expected = accountRestController.putAccount(account1.getId(), "1", "2", "3", "4", "five@example.com");
        Account actual = accountRepository.findOne(account1.getId());
        Assert.assertTrue(expected.matches(actual));

        expected = Account.createAccount("1", "2", "3", "4", "five@example.com");
        expected.setId(account1.getId());
        Assert.assertTrue(expected.matches(actual));
    }

}
