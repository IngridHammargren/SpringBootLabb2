package se.iths.springbootlabb2.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootlabb2.CreateMessageFormData;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.MessageService;
import se.iths.springbootlabb2.services.UserService;

import java.time.Instant;
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


        org.springframework.security.oauth2.core.user.OAuth2User userDetails =
                (org.springframework.security.oauth2.core.user.OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Optional<UserEntity> existingUser = userRepository.findByUserName(userDetails.getAttributes().get("login").toString());
        UserEntity userEntity;
        if (existingUser.isPresent()) {
            userEntity = existingUser.get();
        } else {
            userEntity = new UserEntity();
            userEntity.setGithubId(userDetails.getAttributes().get("id") != null ? Long.parseLong(userDetails.getAttributes().get("id").toString()) : 123L);
            userEntity.setUserName(userDetails.getAttributes().get("login") != null ? userDetails.getAttributes().get("login").toString() : "maxerkmar");
            userEntity.setFirstName(userDetails.getAttributes().get("name") != null ? userDetails.getAttributes().get("name").toString() : "max");
            userEntity.setLastName(userDetails.getAttributes().get("family_name") != null ? userDetails.getAttributes().get("family_name").toString() : "erkmar");
            userEntity.setEmail(userDetails.getAttributes().get("email") != null ? userDetails.getAttributes().get("email").toString() : "max.erkmar@iths.se");
        }

        msg.setUserEntity(userEntity);

        messageService.saveMessage(msg.toEntity());
        return "redirect:/web/messages";
    }

    @PostMapping("messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id) {
        try {
            // Retrieve the message by ID
            Optional<MessageEntity> optionalMessage = messageService.getMessageById(id);

            // Check if the message exists
            if (optionalMessage.isPresent()) {
                // Delete the message
                messageService.deleteMessageById(id);
            } else {
                // If the message doesn't exist, handle the error accordingly
                throw new EntityNotFoundException("Message not found with ID: " + id);
            }
        } catch (EntityNotFoundException e) {
            // Handle the exception, for example, by logging the error or displaying a message to the user
            e.printStackTrace(); // Log the error
        }

        // Redirect back to the messages page
        return "redirect:/web/messages";
    }


}
