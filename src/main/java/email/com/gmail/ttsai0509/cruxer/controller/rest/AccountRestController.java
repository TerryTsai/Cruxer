package email.com.gmail.ttsai0509.cruxer.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.exception.CruxerException;
import email.com.gmail.ttsai0509.cruxer.model.*;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import email.com.gmail.ttsai0509.cruxer.util.DateUtils;
import email.com.gmail.ttsai0509.cruxer.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("/accounts")
public class AccountRestController {

    @Autowired private AccountService accountService;
    @Autowired private AccountRepository accountRepo;

    @Secured({"ROLE_USER"})
    @JsonView(AccountViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Account> getAccounts(Pageable pageable) {

        return accountRepo.findAll(pageable).getContent();

    }

    @Secured({"ROLE_USER"})
    @JsonView(AccountViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String id) {

        return accountRepo.findOne(id);

    }

    @Secured({"ROLE_USER"})
    @JsonView(AccountViews.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Account putAccount(
            @PathVariable String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email
    ) {

        Account account = accountRepo.findOne(id);
        if (!accountService.isCurrentAccount(account))
            throw new AccessDeniedException("Can not modify account.");

        if (username != null) {
            Account usernameAcct = accountRepo.findByUsername(username);
            if (usernameAcct != null)
                throw new CruxerException(username + " already registered.");
            account.setUsername(username);
        }

        if (password != null) {
            String hashword = accountService.generateHashword(password);
            account.setHashword(hashword);
        }

        if (firstname != null) {
            account.setFirstname(firstname);
        }

        if (lastname != null) {
            account.setLastname(lastname);
        }

        if (email != null) {
            account.setEmail(email);
        }

        return accountRepo.save(account);

    }

    @JsonView(AccountViews.Standard.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Account postAccount(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String firstname,
            @RequestParam(required = true) String lastname,
            @RequestParam(required = true) String email
    ) {

        Account usernameAcct = accountRepo.findByUsername(username);
        if (usernameAcct != null)
            throw new CruxerException(username + " already registered.");

        String hashword = accountService.generateHashword(password);

        Account account = Account.createAccount(username, hashword, firstname, lastname, email, Role.USER);

        return accountRepo.save(account);

    }

    @RequestMapping(value ="/reset", method = RequestMethod.GET)
    public void getReset(
            @RequestParam(required = true) String username
    ) {

        Account usernameAcct = accountRepo.findByUsername(username);
        if (username != null)
            throw new CruxerException(username + " not found.");

        String resetToken = accountService.generateResetToken();

        usernameAcct.setResetToken(resetToken);

        usernameAcct.setResetExpire(DateUtils.addNow(DateUtils.Field.DAY, 1));

        accountRepo.save(usernameAcct);

    }

    @RequestMapping(value ="/reset", method = RequestMethod.POST)
    public void postReset(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String resetToken
    ) {

        Account usernameAcct = accountRepo.findByUsername(username);
        if (username != null)
            throw new CruxerException(username + " not found.");

        if (!resetToken.equals(usernameAcct.getResetToken())) {
            throw new CruxerException("Invalid reset token.");
        }

        Date resetExpire = usernameAcct.getResetExpire();
        if (resetExpire.before(new Date())) {
            throw new CruxerException("Expired token.");
        }

        String hashword = accountService.generateHashword(password);
        usernameAcct.setHashword(hashword);

        accountRepo.save(usernameAcct);

    }

}
