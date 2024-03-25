package se.iths.springbootlabb2.repositories;

import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootlabb2.entities.UserEntity;

public interface UserRepository  extends ListCrudRepository<UserEntity, Long> {
}
