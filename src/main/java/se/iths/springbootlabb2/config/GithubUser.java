package se.iths.springbootlabb2.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser {

    private String login;
    private Long id;
    private String name;
    private String url;
    private String avatarUrl;
    private String email;

}

