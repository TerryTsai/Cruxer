package email.com.gmail.ttsai0509.cruxer.rest;

import email.com.gmail.ttsai0509.cruxer.Cruxer;
import email.com.gmail.ttsai0509.cruxer.config.SecurityConfig;
import email.com.gmail.ttsai0509.cruxer.config.production.WebMvcConfig;
import email.com.gmail.ttsai0509.cruxer.controller.rest.AccountRestController;
import email.com.gmail.ttsai0509.cruxer.controller.rest.HoldRestController;
import email.com.gmail.ttsai0509.cruxer.controller.rest.WallRestController;
import email.com.gmail.ttsai0509.cruxer.utils.AssertEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Cruxer.class, SecurityConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class ControllerAccessTest {

    private PageRequest pageable = new PageRequest(0, 1);
    private MultipartFile file = new MockMultipartFile("test", new byte[]{1, 0, 1, 0});

    @Autowired private AccountRestController accountRestController;
    @Autowired private HoldRestController holdRestController;
    @Autowired private WallRestController wallRestController;

    @Test
    public void accounts() {
        AssertEx.exception(
                AuthenticationCredentialsNotFoundException.class,
                () -> accountRestController.getAccounts(null),
                () -> accountRestController.getAccount(null),
                () -> accountRestController.putAccount(null, null, null, null, null, null)
        );
    }

    @Test
    public void holds() {
        AssertEx.exception(
                AuthenticationCredentialsNotFoundException.class,
                () -> holdRestController.getHold(null),
                () -> holdRestController.postHold(null),
                () -> holdRestController.putHold(null, null),
                () -> holdRestController.getHolds(null)
        );
    }

    @Test
    public void walls() {
        AssertEx.exception(
                AuthenticationCredentialsNotFoundException.class,
                () -> wallRestController.getWall(null),
                () -> wallRestController.postWall(null),
                () -> wallRestController.putWall(null, null),
                () -> wallRestController.getWalls(null)
        );
    }

}
