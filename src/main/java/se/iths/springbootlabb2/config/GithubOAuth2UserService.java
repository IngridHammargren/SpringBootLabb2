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

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GithubService gitHubService;
    UserRepository userRepository;
    UserService userService;

    @Autowired
    public GithubOAuth2UserService(GithubService gitHubService, UserRepository userRepository) {
        this.gitHubService = gitHubService;
        this.userRepository = userRepository;
    }

    //https://dev.to/relive27/spring-security-oauth2-login-51lj
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        // Retrieve the access token
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        // Make a request to the GitHub API to fetch the email address
        List<Email> result = gitHubService.getEmails(accessToken);

        GithubUser gitHubUser = new GithubUser();
        gitHubUser.setUserId(((Integer) attributes.get("id")).longValue());
        gitHubUser.setName((String) attributes.get("name"));
        gitHubUser.setUrl((String) attributes.get("html_url"));
        gitHubUser.setAvatarUrl((String) attributes.get("avatar_url"));
        gitHubUser.setLogin((String) attributes.get("login"));
        gitHubUser.setEmail(result.getFirst().email());        updateUser(gitHubUser);

        return oidcUser;
    }



    private void updateUser(GithubUser gitUser) {
        logger.info("User detected, {}, {}", gitUser.getLogin(), gitUser.getName());
        var user = userService.findByGithubId(gitUser.getUserId());
        if (user == null) {
            logger.info("New user detected, {}, {}", gitUser.getLogin(), gitUser.getName());
            user = new UserEntity();
        }

        user.setGithubId(gitUser.getUserId());

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

}

