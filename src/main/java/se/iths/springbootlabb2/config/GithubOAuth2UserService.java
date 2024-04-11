package se.iths.springbootlabb2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GithubService githubService;

    public GithubOAuth2UserService(GithubService githubService) { this.githubService = githubService; }

    //https://dev.to/relive27/spring-security-oauth2-login-51lj
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        // Retrieve the access token
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        // Make a request to the GitHub API to fetch the email address
        List<Email> result = GithubService.getEmails(accessToken);
    }
}

