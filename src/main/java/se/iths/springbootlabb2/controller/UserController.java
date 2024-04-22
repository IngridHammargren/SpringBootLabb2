package se.iths.springbootlabb2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.springbootlabb2.UserFormData;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;

@Controller
@RequestMapping("/web")
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("profile")
    public String showProfile(Model model, @AuthenticationPrincipal OAuth2User principal) {
        UserEntity user = userRepository.findByGithubId( ((Integer) principal.getAttribute("id")).longValue());

        model.addAttribute("user", user);

        model.addAttribute("userFormData", new UserFormData(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfilePicture()));
        return "profile";
    }
}

