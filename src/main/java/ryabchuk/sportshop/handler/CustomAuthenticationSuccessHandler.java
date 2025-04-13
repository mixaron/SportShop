package ryabchuk.sportshop.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ryabchuk.sportshop.config.LoginAttemptService;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public CustomAuthenticationSuccessHandler(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
        setDefaultTargetUrl("/products");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        String username = authentication.getName();
        loginAttemptService.loginSucceeded(username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isModerator = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"));

        if (isAdmin || isModerator) {
            setDefaultTargetUrl("/admin");
        } else {
            setDefaultTargetUrl("/products");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}