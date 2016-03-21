package email.com.gmail.ttsai0509.cruxer.controller;

import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteController {

    @Autowired private AccountService accountService;

    @RequestMapping(value = {"", "/", "/index.html"}, method = RequestMethod.GET)
    public String getIndex() {
        if (accountService.getCurrentAccount() == null)
            return "index";
        else
            return "redirect:/home.html";
    }

    @RequestMapping(value = {"/about.html"}, method = RequestMethod.GET)
    public String getAbout() {
        return "about";
    }

    @RequestMapping(value = {"/login.html"}, method = RequestMethod.GET)
    public String getLogin() {
        return "login";
    }

    @RequestMapping(value = {"/register.html"}, method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = {"/home.html"}, method = RequestMethod.GET)
    public String getHome() {
        return "home";
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = {"/design.html"}, method = RequestMethod.GET)
    public String getDesign(@RequestParam(required = false) String id, Model model) {
        if (id != null && !id.isEmpty())
            model.addAttribute("id", id);
        return "design";
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = {"/upload.html"}, method = RequestMethod.GET)
    public String getUpload() {
        return "upload";
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = {"/account.html"}, method = RequestMethod.GET)
    public String getAccount() {
        return "account";
    }

}
