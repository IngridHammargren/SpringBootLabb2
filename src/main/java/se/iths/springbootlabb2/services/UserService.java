package se.iths.springbootlabb2.services;

import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserEntity findByGithubId(Long github_id) {
        return userRepository.findByGithubId(github_id);
    }

}




