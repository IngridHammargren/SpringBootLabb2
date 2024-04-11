package se.iths.springbootlabb2.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import se.iths.springbootlabb2.entities.UserEntity;


import java.util.List;
import java.util.Optional;

public interface UserRepository  extends ListCrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);

    UserEntity findByGithubId(Long githubId);
}

