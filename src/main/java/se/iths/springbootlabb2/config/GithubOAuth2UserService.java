package se.iths.springbootlabb2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GithubService gitHubService;
    UserRepository userRepository;
    UserService userService;

    @Autowired
    public GithubOAuth2UserService(GithubService gitHubService, UserRepository userRepository, UserService userService) {
        this.gitHubService = gitHubService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //https://dev.to/relive27/spring-security-oauth2-login-51lj
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        Long gitId = (Long) attributes.get("id");
        Optional<UserEntity> authenticatedUser = Optional.ofNullable(userRepository.findByGithubId(gitId));


        if (authenticatedUser.isEmpty()) {
            UserEntity userEntity = createUserFromAttributes(attributes, userRequest);
            userRepository.save(user);
        }

        return oauth2User;

    }
        // Retrieve the access token
       // OAuth2AccessToken accessToken = userRequest.getAccessToken();

        // Make a request to the GitHub API to fetch the email address
        //List<Email> result = gitHubService.getEmails(accessToken);

    private UserEntity createUserFromAttributes(Map<String, Object> attributes, OAuth2UserRequest userRequest) {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        UserEntity userEntity = new UserEntity();
        userEntity.setGithubId(((Integer) attributes.get("id")).longValue());
        var names = s.getName().split(" ");
        user.setFirstName(names[0]);
        if (names.length > 1) {
            user.setLastName(names[1]);
        } else {
            user.setLastName("");
        }

        userEntity.setName((String) attributes.get("name"));
        userEntity.setUrl((String) attributes.get("html_url"));
        userEntity.setAvatarUrl((String) attributes.get("avatar_url"));
        userEntity.setLogin((String) attributes.get("login"));
        userEntity.setEmail((String) attributes.get("email"));

        List<Email> result = gitHubService.getEmails(accessToken);
        for (Email email : result) {
            if (email.primary()) {
                String gitHubEmail = email.email();
                userEntity.setEmail(gitHubEmail);
                break;
            }
        }

        return userEntity;
    }



    private void updateUser(GithubUser gitUser) {
        logger.info("User detected, {}, {}", gitUser.getLogin(), gitUser.getName());
        UserEntity user = userService.findByGithubId(gitUser.getUserId());
        if (user == null) {
            logger.info("New user detected, {}, {}", gitUser.getLogin(), gitUser.getName());
            user = new UserEntity();
            user.setGithubId(gitUser.getUserId());
        }

        var names = gitUser.getName().split(" ");
        user.setFirstName(names[0]);
        if (names.length > 1) {
            user.setLastName(names[1]);
        } else {
            user.setLastName("");
        }
        user.setUserName(gitUser.getLogin());
        user.setProfilePicture(gitUser.getAvatarUrl());

        userRepository.save(user);
    }

    public UserEntity findOrCreateUserFromGithub(OAuth2User userDetails) {
        // Hämta GitHub-användaruppgifter från OAuth2-användarobjektet
        String githubId = userDetails.getAttribute("id");
        String username = userDetails.getAttribute("login");
        String name = userDetails.getAttribute("name");
        String email = userDetails.getAttribute("email");

        // Kontrollera om e-postadressen är null eller tom
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid or missing email address.");
        }

        // Försök hitta användaren i databasen baserat på GitHub-ID
        UserEntity user = userRepository.findByGithubId(Long.parseLong(githubId));

        // Om användaren inte finns, skapa en ny användare
        if (user == null) {
            user = new UserEntity();
            user.setGithubId(Long.parseLong(githubId));
            user.setUserName(username);
            user.setFirstName(name != null ? name.split(" ")[0] : "");
            user.setLastName(name != null ? name.split(" ")[1] : "");
            user.setEmail(email); // Tilldela e-postadressen till användaren
        }

        // Spara eller uppdatera användaren i databasen
        userRepository.save(user);

        return user;
    }

}

