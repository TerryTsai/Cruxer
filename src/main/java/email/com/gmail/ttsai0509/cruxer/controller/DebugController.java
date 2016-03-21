package email.com.gmail.ttsai0509.cruxer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Hold;
import email.com.gmail.ttsai0509.cruxer.model.Route;
import email.com.gmail.ttsai0509.cruxer.model.Wall;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.repository.HoldRepository;
import email.com.gmail.ttsai0509.cruxer.repository.RouteRepository;
import email.com.gmail.ttsai0509.cruxer.repository.WallRepository;
import email.com.gmail.ttsai0509.cruxer.view.AccountViews;
import email.com.gmail.ttsai0509.cruxer.view.HoldViews;
import email.com.gmail.ttsai0509.cruxer.view.RouteViews;
import email.com.gmail.ttsai0509.cruxer.view.WallViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired private AccountRepository accountRepository;
    @Autowired private RouteRepository routeRepository;
    @Autowired private HoldRepository holdRepository;
    @Autowired private WallRepository wallRepository;

    @JsonView(AccountViews.Standard.class)
    @RequestMapping("/accounts")
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @JsonView(RouteViews.Complete.class)
    @RequestMapping("/routes")
    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @JsonView(HoldViews.Debug.class)
    @RequestMapping("/holds")
    public List<Hold> getHolds() {
        return holdRepository.findAll();
    }

    @JsonView(WallViews.Debug.class)
    @RequestMapping("/walls")
    public List<Wall> getWalls() {
        return wallRepository.findAll();
    }
}
