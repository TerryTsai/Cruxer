package email.com.gmail.ttsai0509.cruxer.controller;

import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
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

    @RequestMapping("/accounts")
    public List<Account> getAccounts() {

        return accountRepository.findAll();

    }

}
