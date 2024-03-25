package se.iths.springbootlabb2.repositories;

import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootlabb2.entities.MessageEntity;

public interface MessageRepository extends ListCrudRepository<MessageEntity, Long> {
}
