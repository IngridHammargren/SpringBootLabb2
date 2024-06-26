package se.iths.springbootlabb2.config;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class GithubService {

    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.restClient = restClient;
    }

    //https://www.baeldung.com/spring-retry
    @Retryable
    public List<Email> getEmails(OAuth2AccessToken accessToken) {
        return restClient.get()
                .uri("https://api.github.com/user/emails")
                .headers(headers -> headers.setBearerAuth(accessToken.getTokenValue()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}

