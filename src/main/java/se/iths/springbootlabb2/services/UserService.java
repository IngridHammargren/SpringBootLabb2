package se.iths.springbootlabb2.services;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.repositories.UserRepository;
import se.iths.springbootlabb2.entities.UserEntity;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users")
    public List<UserEntity> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Cacheable(value = "users")
    public List<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(value = "users")
    public List<UserEntity> findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Cacheable(value = "users")
    public List<UserEntity> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @CacheEvict(value = "users", key = "'id:' + #id")
    public void updateProfilePictureByUserName(Long id, String profilePicture) {
        userRepository.updateProfilePictureByUserName(id, profilePicture);
    }

    @CacheEvict(value = "users", key = "'id:' + #id")
    public void updateUserNameByUserName(Long id, String userName) {
        userRepository.updateUserNameByUserName(id, userName);
    }

    @CacheEvict(value = "users", key = "'id:' + #id")
    public void updateEmailById(Long id, String email) {
        userRepository.updateEmailById(id, email);
    }

}
