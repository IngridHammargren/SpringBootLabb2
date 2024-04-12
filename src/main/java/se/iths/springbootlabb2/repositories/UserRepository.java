package se.iths.springbootlabb2.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.iths.springbootlabb2.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends ListCrudRepository<UserEntity, Long> {
    UserEntity findByGithubId(Long githubId);

    Optional<UserEntity> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.userName = :userName WHERE u.id = :id")
    void updateUserNameById(@Param("id") Long id, @Param("userName") String userName);


    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.email = :email WHERE u.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("email") String email);

}

