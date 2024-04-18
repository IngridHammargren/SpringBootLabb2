package se.iths.springbootlabb2.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootlabb2.CreateMessageFormData;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.MessageService;

import se.iths.springbootlabb2.services.TranslateService;

import java.time.Instant;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {

    MessageService messageService;
    UserRepository userRepository;
    TranslateService translateService;

    public WebController(MessageService messageService, UserRepository userRepository, TranslateService translateService) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.translateService= translateService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect: /web/messages";
    }

    @GetMapping("/secured")
    public String secured() {
        return "secured";
    }
    @Operation(summary = "Get all messages")
    @GetMapping("messages")
    public String getAllMessages(Model model, HttpServletRequest httpServletRequest) {
        Iterable<MessageEntity> messages = messageService.getAllMessages();
        Collections.reverse((List<MessageEntity>) messages);
        model.addAttribute("messages", messages);
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "messages";
    }

    @Operation(summary = "Show create message form")
    @GetMapping("create")
    public String showCreateForm(Model model) {
        model.addAttribute("messageContent", new CreateMessageFormData());
        return "create";
    }

    @Operation(summary = "Create a message")
    @PostMapping("create")
    public String createMessage(@ModelAttribute("messageContent") CreateMessageFormData msg,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return "create";

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByGithubId(Long.valueOf(username));

        msg.setUserEntity(user);

        messageService.saveMessage(msg.toEntity());
        return "redirect:/web/messages";
    }

    @PostMapping("/translate/{id}")
    public String translateMessage(@PathVariable Long id, Model model, HttpServletRequest httpServletRequest) {
        Optional<MessageEntity> optionalMessage = messageService.getMessageById(id);

        if (optionalMessage.isPresent()) {
            MessageEntity message = optionalMessage.get();
            String translatedMessage = translateService.translateMessage(message.getContent());
            message.setContent(translatedMessage);
            messageService.saveMessage(message);
        } else {

        }

        Iterable<MessageEntity> messages = messageService.getAllMessages();
        Collections.reverse((List<MessageEntity>) messages);
        model.addAttribute("messages", messages);
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "messages";
    }



    @Operation(summary = "Delete a message")

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

    @Operation(summary = "Show edit message form")
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
    @Operation(summary = "Edit a message")
    @PostMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") Long id, @ModelAttribute("editedMessage") CreateMessageFormData editedMessage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        Optional<MessageEntity> messageOptional = messageService.getMessageById(id);
        if (messageOptional.isPresent()) {
            MessageEntity message = messageOptional.get();
            message.setContent(editedMessage.getContent());
            messageService.saveMessage(message);
        }
        return "redirect:/web/messages";
    }
    @Operation(summary = "Logout")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/login?logout";
    }

}



