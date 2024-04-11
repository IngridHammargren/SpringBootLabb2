package se.iths.springbootlabb2.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import se.iths.springbootlabb2.CreateMessageFormData;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.MessageService;


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

        OAuth2User userDetails = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDetails.getAttributes().get("login").toString());
        UserEntity user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new UserEntity();
            user.setGithubId(Long.parseLong(userDetails.getAttributes().get("id").toString()));
            user.setUserName(userDetails.getAttributes().get("login").toString());
            user.setFirstName(userDetails.getAttributes().get("name").toString());
            user.setLastName(userDetails.getAttributes().get("family_name").toString());
            user.setEmail(userDetails.getAttributes().get("email") != null ? userDetails.getAttributes().get("email").toString() : "max.erkmar@iths.se");
        }

        msg.setUserEntity(user);

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
