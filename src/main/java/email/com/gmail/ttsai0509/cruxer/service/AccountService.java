package email.com.gmail.ttsai0509.cruxer.service;

import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.repository.AccountRepository;
import email.com.gmail.ttsai0509.cruxer.util.SecurityUtils;
import email.com.gmail.ttsai0509.cruxer.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired private AccountRepository accountRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean isCurrentAccount(Account account) {

        if (account == null)
            return false;

        String username = SecurityUtils.getCurrentUsername();
        if (username == null || username.isEmpty())
            return false;

        Account current = accountRepo.findByUsername(username);
        if (current == null)
            return false;

        return current.matches(account);

    }

    @Transactional(readOnly = true)
    public Account getCurrentAccount() {

        String username = SecurityUtils.getCurrentUsername();

        if (username == null || username.isEmpty())
            return null;
        else
            return accountRepo.findByUsername(username);

    }

    @Transactional(readOnly = true)
    public String generateHashword(String password) {

        return passwordEncoder.encode(password);

    }

    @Transactional(readOnly = true)
    public String generateResetToken() {

        return StringUtils.randomString(30, StringUtils.ALL);

    }

}
