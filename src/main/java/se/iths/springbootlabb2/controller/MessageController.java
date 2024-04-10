package se.iths.springbootlabb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.services.MessageService;

@Controller
@RequestMapping("/apa/apa")
public class MessageController {

    MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping()
    public String getAllMessages(Model model) {
        Iterable<MessageEntity> messages = messageService.getAllMessages();
        model.addAttribute("messages", messages);
        return "messages";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("message", new MessageEntity());
        return "create";
    }

    @PostMapping("/create")
    public String createMessage(@ModelAttribute("message") MessageEntity messageEntity) {
        messageService.saveMessage(messageEntity);
        return "redirect:/messages/list";
    }
}