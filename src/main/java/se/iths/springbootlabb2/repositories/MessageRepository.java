package se.iths.springbootlabb2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootlabb2.entities.MessageEntity;


import java.util.List;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

}
