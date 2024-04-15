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
import java.util.Optional;

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
        OAuth2User  oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes =  oauth2User.getAttributes();

        Integer githubId = (Integer) attributes.get("id");
        Optional<UserEntity> authenticatedUser = Optional.ofNullable(userRepository.findByGithubId(Long.valueOf(githubId)));

        if (authenticatedUser.isEmpty()) {
            UserEntity userEntity = createGitUser(attributes, userRequest);
            userRepository.save(userEntity);
        }
        return oauth2User;
    }

    private UserEntity createGitUser(Map<String, Object> attributes, OAuth2UserRequest userRequest) {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        UserEntity userEntity = new UserEntity();

        userEntity.setGithubId( ((Integer) attributes.get("id")).longValue());        userEntity.setUserName((String) attributes.get("login"));
        userEntity.setProfilePicture((String) attributes.get("avatar_url"));

        String name = (String) attributes.get("name");
        var names = name.split(" ");
        if (names.length > 0){
            userEntity.setFirstName(names[0]);
            if (names.length > 1){
                StringBuilder lastNameBuilder = new StringBuilder();
                for (int i = 1; i < names.length; i++) {
                    if (i > 1) {
                        lastNameBuilder.append(" ");
                    }
                    lastNameBuilder.append(names[i]);
                }
                userEntity.setLastName(lastNameBuilder.toString());
            } else {
                userEntity.setLastName("");
            }
        }

        userEntity.setEmail((String) attributes.get("email"));
        List<Email> emails = githubService.getEmails(accessToken);
        for (Email mail : emails) {
            if (mail.primary()) {
                String userEmail = mail.email();
                userEntity.setEmail(userEmail);
                break;
            }
        }
        return userEntity;
    }
}

