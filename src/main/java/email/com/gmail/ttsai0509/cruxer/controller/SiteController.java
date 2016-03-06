package email.com.gmail.ttsai0509.cruxer.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SiteController {

    @RequestMapping(value = {"", "/", "/index.html"}, method = RequestMethod.GET)
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = {"/register.html"}, method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = {"/login.html"}, method = RequestMethod.GET)
    public String getLogin() {
        return "login";
    }

    @RequestMapping(value = {"/viewer.html"}, method = RequestMethod.GET)
    public String getViewer() {
        return "viewer";
    }

}
