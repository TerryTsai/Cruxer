package email.com.gmail.ttsai0509.cruxer.intercept;

import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccountInjector extends HandlerInterceptorAdapter {

    @Autowired
    private AccountService accountService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {

        Account account = accountService.getCurrentAccount();

        if (modelAndView != null && account != null)
            modelAndView.addObject("account", account);

        super.postHandle(request, response, handler, modelAndView);

    }
}
