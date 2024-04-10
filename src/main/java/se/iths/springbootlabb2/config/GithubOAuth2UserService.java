package se.iths.springbootlabb2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GithubService gitHubService;

    public GithubOAuth2UserService(GithubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    //https://dev.to/relive27/spring-security-oauth2-login-51lj
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        GithubUser gitHubUser = new GithubUser();
        gitHubUser.setUserId(String.valueOf(attributes.get("id")));
        gitHubUser.setName((String) attributes.get("name"));
        gitHubUser.setUrl((String) attributes.get("html_url"));
        gitHubUser.setAvatarUrl((String) attributes.get("avatar_url"));
        gitHubUser.setLogin((String) attributes.get("login"));
        updateUser(gitHubUser);

        return oidcUser;
    }

    private void updateUser(GithubUser gitUser) {
        logger.info("User detected, {}, {}", gitUser.getLogin(), gitUser.getName());
//        var user = userInfoRepository.findByUserId(gitUser.getUserId());
//        if (user == null) {
//            logger.info("New user detected, {}, {}", gitUser.getLogin(), gitUser.getName());
//            user = new UserInfo();
//        }
//
//        user.setUserId(gitUser.getUserId());
//        user.setName(gitUser.getName());
//        user.setUrl(gitUser.getUrl());
//        user.setLogin(gitUser.getLogin());
//        user.setAvatarUrl(gitUser.getAvatarUrl());
//
//        userInfoRepository.save(user);
    }
}

