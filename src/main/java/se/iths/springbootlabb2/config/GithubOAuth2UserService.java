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
import java.util.List;
import java.util.Map;

@Service
public class GithubOAuth2UserService
        extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GithubService githubService;
    UserRepository userRepository;

    @Autowired
    public GithubOAuth2UserService(GithubService githubService, UserRepository userRepository) {
        this.githubService = githubService;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        List<Email> result = githubService.getEmails(accessToken);

        GithubUser githubUser = new GithubUser();
        githubUser.setUserId(((Integer) attributes.get("id")).longValue());
        githubUser.setName((String) attributes.get("name"));
        githubUser.setUrl((String) attributes.get("html_url"));
        githubUser.setAvatarUrl((String) attributes.get("avatar_url"));
        githubUser.setLogin((String) attributes.get("login"));
        githubUser.setEmail(result.getFirst().email());
        updateUser(githubUser);

        return oidcUser;
    }

    private void updateUser(GithubUser gitUser) {
        logger.info("User detected, {}, {}", gitUser.getLogin(), gitUser.getName());
        UserEntity user = userRepository.findByGithubId(gitUser.getUserId());

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
}

