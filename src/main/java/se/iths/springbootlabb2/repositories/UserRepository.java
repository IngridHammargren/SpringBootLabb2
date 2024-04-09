package se.iths.springbootlabb2.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import se.iths.springbootlabb2.entities.UserEntity;


import java.util.List;

public interface UserRepository  extends ListCrudRepository<UserEntity, Long> {

}

