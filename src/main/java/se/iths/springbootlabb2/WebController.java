package se.iths.springbootlabb2;

import org.springframework.web.bind.annotation.GetMapping;

public class WebController {
    @GetMapping("/web/friend")
    public String friends(){
        //var friend = friendRepository.findAll();
        return "users";
    }
}
