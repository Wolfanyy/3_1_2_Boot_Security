package boot_security.config;

import boot_security.model.RoleName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SuccessUserHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        if (hasRole(authentication, RoleName.ADMIN)) {
            return "/admin";
        }

        if (hasRole(authentication, RoleName.USER)) {
            return "/user";
        }

        return "/";
    }

    private boolean hasRole(Authentication authentication, RoleName role) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()));
    }
}