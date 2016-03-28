package email.com.gmail.ttsai0509.cruxer.rest;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.ProdWebConfig;
import email.com.gmail.ttsai0509.cruxer.model.Role;
import email.com.gmail.ttsai0509.cruxer.controller.rest.WallRestController;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Wall;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.repository.WallRepository;
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
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, ProdWebConfig.class})
@WebAppConfiguration
public class WallControllerTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private WallRepository wallRepository;
    @Autowired private WallRestController wallRestController;

    private Account account;

    @Before
    public void before() {
        account = Account.createAccount("jdoe05", "abc123", "John", "Doe", "jdoe05@example.com", Role.USER);
        accountRepository.save(account);
    }

    @After
    public void after() {
        wallRepository.deleteAllInBatch();
        accountRepository.delete(account);
    }

    @Test
    public void authenticationEndpointsTest() {
        AssertEx.exception(
                AuthenticationCredentialsNotFoundException.class,
                () -> wallRestController.getWall(null),
                () -> wallRestController.postWall(null, null),
                () -> wallRestController.putWall(null, null),
                () -> wallRestController.getWalls(null)
        );
    }

    @Test
    @WithMockUser(username = "jdoe05", password = "abc123", roles = {"USER"})
    public void getWallPass() {
        Wall expected = wallRepository.save(new Wall(account, "model.obj", ""));
        Wall actual = wallRestController.getWall(expected.getId());
        Assert.assertTrue(expected.matches(actual));
    }

}
