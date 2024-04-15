package se.iths.springbootlabb2.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootlabb2.CreateMessageFormData;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.MessageService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {

    MessageService messageService;
    UserRepository userRepository;

    public WebController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(){
        return "redirect: /web/messages";
    }

    @GetMapping("/secured")
    public String secured(){
        return "secured";
    }

    @GetMapping("messages")
    public String getAllMessages(Model model) {
        Iterable<MessageEntity> messages = messageService.getAllMessages();
        Collections.reverse((List<MessageEntity>) messages);
        model.addAttribute("messages", messages);
        return "messages";
    }
    @GetMapping("create")
    public String showCreateForm(Model model) {
        model.addAttribute("messageContent", new CreateMessageFormData());
        return "create";
    }
/*
    @PostMapping("create")
    public String createMessage(@ModelAttribute("messageContent") CreateMessageFormData msg,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) return "create";

        OAuth2User userDetails = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDetails.getAttributes().get("login").toString());
        UserEntity user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new UserEntity();
            var name = userDetails.getAttributes().get("name").toString().split(" ");
            user.setGithubId(Long.parseLong(userDetails.getAttributes().get("id").toString()));
            user.setUserName(userDetails.getAttributes().get("login").toString());
            user.setFirstName(name[0]);
            String lastName = (name.length > 1) ? name[1] : "";
            user.setLastName(lastName);
            user.setEmail( "max.erkmar@iths.se");
        }

        msg.setUserEntity(user);

        messageService.saveMessage(msg.toEntity());
        return "redirect:/web/messages";
    }


 */

    @PostMapping("messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id) {
        try {
            Optional<MessageEntity> optionalMessage = messageService.getMessageById(id);
            if (optionalMessage.isPresent()) messageService.deleteMessageById(id);
            else throw new EntityNotFoundException("Message not found with ID: " + id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
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
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/login?logout";
    }

}
