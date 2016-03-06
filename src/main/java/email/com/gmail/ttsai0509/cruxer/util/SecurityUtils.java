package email.com.gmail.ttsai0509.cruxer.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static String getCurrentUsername() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null)
            return "";

        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null)
            return "";

        Object principal = authentication.getPrincipal();
        if (principal == null)
            return "";
        else if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        else if (principal instanceof String)
            return (String) principal;
        else
            return "";

    }

}
