package se.iths.springbootlabb2.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import se.iths.springbootlabb2.CreateMessageFormData;
import se.iths.springbootlabb2.config.GithubOAuth2UserService;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.MessageService;
import se.iths.springbootlabb2.services.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {

    GithubOAuth2UserService githubOAuth2UserService;
    MessageService messageService;
    UserService userService;

    public WebController(GithubOAuth2UserService githubOAuth2UserService, MessageService messageService, UserService userService) {
        this.githubOAuth2UserService = githubOAuth2UserService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "secured";
    }

    @GetMapping("messages")
    public String getAllMessages(Model model) {
        Iterable<MessageEntity> messages = messageService.getAllMessages();
        model.addAttribute("messages", messages);
        return "messages";
    }
    @GetMapping("create")
    public String showCreateForm(Model model) {
        model.addAttribute("messageContent", new CreateMessageFormData());
        return "create";
    }

    @PostMapping("create")
    public String createMessage(@ModelAttribute("messageContent") CreateMessageFormData msg,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println("Has error");
            return "create";
        }

        // Hämta användaruppgifter från autentiseringen
        OAuth2User userDetails = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Hämta eller skapa en ny UserEntity baserat på GitHub-uppgifterna
        UserEntity user = githubOAuth2UserService.findOrCreateUserFromGithub(userDetails);

        // Ange användaren för meddelandet
        msg.setUserEntity(user);

        // Spara meddelandet i databasen
        messageService.saveMessage(msg.toEntity());

        // Omdirigera till meddelandelistan
        return "redirect:/web/messages";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<MessageEntity> messageOptional = messageService.getMessageById(id);
        if (messageOptional.isPresent()) {
            MessageEntity message = messageOptional.get();
            CreateMessageFormData editedMessage = new CreateMessageFormData();
            editedMessage.setContent(message.getContent());
            model.addAttribute("messageId", id);
            model.addAttribute("editedMessage", editedMessage);
            return "edit";
        } else {
            return "redirect:/web/messages";
        }
    }
    @PostMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") Long id, @ModelAttribute("editedMessage") CreateMessageFormData editedMessage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {return "edit";}
        Optional<MessageEntity> messageOptional = messageService.getMessageById(id);
        if (messageOptional.isPresent()) {
            MessageEntity message = messageOptional.get();
            message.setContent(editedMessage.getContent());
            messageService.saveMessage(message);
        }
        return "redirect:/web/messages";
    }
}
