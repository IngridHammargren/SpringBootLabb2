package se.iths.springbootlabb2.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.entities.UserEntity;
import se.iths.springbootlabb2.repositories.UserRepository;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findByGithubId(Long githubId) {
        return userRepository.findByGithubId(githubId);
    }

    public Optional<UserEntity> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void updateUserNameById(Long id, String userName) {
        userRepository.updateUserNameById(id, userName);
    }

    public void updateEmailById(Long id, String email) {
        userRepository.updateEmailById(id, email);
    }

}




