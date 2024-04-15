package se.iths.springbootlabb2.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.iths.springbootlabb2.entities.UserEntity;
import java.util.Optional;

@Repository
public interface UserRepository  extends ListCrudRepository<UserEntity, Long> {
    UserEntity findByGithubId(Long githubId);

    Optional<UserEntity> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.firstName = :firstName, u.lastName = :lastName, u.email = :email, u.githubId = :githubId, u.profilePicture = :profilePicture where u.id = :id")
    void updateUser(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @Param("githubId") Long githubId, @Param("profilePicture") String profilePicture);
}



