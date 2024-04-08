package se.iths.springbootlabb2.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.UserService;

import java.util.List;


@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;

    }


    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<List<UserEntity>> findByUserName(@PathVariable String userName) {
        List<UserEntity> users = userService.findByUserName(userName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/first-name/{firstName}")
    public ResponseEntity<List<UserEntity>> getUsersByFirstName(@PathVariable String firstName) {
        List<UserEntity> users = userService.findByFirstName(firstName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/last-name/{lastName}")
    public ResponseEntity<List<UserEntity>> getUsersByLastName(@PathVariable String lastName) {
        List<UserEntity> users = userService.findByLastName(lastName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<List<UserEntity>> getUsersByEmail(@PathVariable String email) {
        List<UserEntity> users = userService.findByEmail(email);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/email/{id}")
    public ResponseEntity<String> updateUserEmailById(@PathVariable Long id, @RequestParam String email) {
        try {
            userService.updateEmailById(id, email);
            return ResponseEntity.ok("Användarens e-postadress uppdaterad");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Misslyckades med att uppdatera användarens e-postadress");
        }
    }

    @PostMapping("/users/email")
    public ResponseEntity<String> saveUserEmail(@RequestBody UserEntity user) {
        try {
            userService.saveEmail(user);
            return ResponseEntity.ok("Användarens e-postadress sparad");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Misslyckades med att spara användarens e-postadress");
        }
    }


    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserEntity user) {
        try {
            userService.createUser(user.getUserName(), user.getProfilePicture(), user.getFirstName(), user.getLastName(), user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Användare skapad");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Misslyckades med att skapa användare");
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        try {
            userService.updateUser(id, user);
            return ResponseEntity.ok("Användarinformation uppdaterad");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Misslyckades med att uppdatera användarinformation");
        }
    }
}




