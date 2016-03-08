package email.com.gmail.ttsai0509.cruxer.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SiteController {

    @RequestMapping(value = {"", "/", "/index.html"}, method = RequestMethod.GET)
    public String getIndex() {
        return "index";
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = {"/viewer.html"}, method = RequestMethod.GET)
    public String getViewer() {
        return "viewer";
    }

    @RequestMapping(value = {"/about.html"}, method = RequestMethod.GET)
    public String getAbout() {
        return "about";
    }

    @RequestMapping(value = {"/register.html"}, method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = {"/login.html"}, method = RequestMethod.GET)
    public String getLogin() {
        return "login";
    }

}
