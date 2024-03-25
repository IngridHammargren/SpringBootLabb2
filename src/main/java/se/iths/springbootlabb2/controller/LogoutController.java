package se.iths.springbootlabb2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public <HttpServletRequest, HttpServletResponse> String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Invalidate session
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout((jakarta.servlet.http.HttpServletRequest) request, (jakarta.servlet.http.HttpServletResponse) response, authentication);
        }
        // Redirect user to logout page or home page
        return "redirect:/login?logout"; // Redirect to the login page with a logout parameter
    }
}
