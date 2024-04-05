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

    @Cacheable(value = "userName")
    public List<UserEntity> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Cacheable(value = "firstName")
    public List<UserEntity> findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Cacheable(value = "lastName")
    public List<UserEntity> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @Cacheable(value = "email")
    public List<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @CacheEvict(value= "email", allEntries = true)
    public void updateEmailById(Long id, String email) {
        userRepository.updateEmailById(id, email);
    }

    @CacheEvict(value = "email", allEntries = true)
    public void saveEmail(UserEntity user) {
        userRepository.save(user);
    }

    @CacheEvict(value= "userName", allEntries = true)
    public void updateUserNameByUserName(Long id, String userName) {
        userRepository.updateUserNameByUserName(id, userName);
    }

    @CacheEvict(value = "userName", allEntries = true)
    public void saveUserName(UserEntity user) {
        userRepository.save(user);
    }

}
