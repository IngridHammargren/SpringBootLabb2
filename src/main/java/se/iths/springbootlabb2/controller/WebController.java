package se.iths.springbootlabb2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {


    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "secured";
    }
}
