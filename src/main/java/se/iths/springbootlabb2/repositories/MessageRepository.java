package se.iths.springbootlabb2.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootlabb2.entities.MessageEntity;
import java.awt.print.Pageable;
import java.util.List;

public interface MessageRepository extends ListCrudRepository<MessageEntity, Long> {

/*
    @Query ("SELECT m FROM MessageEntity m WHERE m.userEntity.id = :userId")
    List<MessageEntity> findByUserId(Long userId);

    @Query("SELECT m FROM MessageEntity m WHERE m.userEntity.id = :userId AND m.isPublic = true")
    List<MessageEntity> findPublicMessagesByUserId(Long userId);
*/
    @Query("SELECT m FROM MessageEntity m WHERE m.isPublic = true ORDER BY m.createdDate DESC")
    List<MessageEntity> findLatestMessagesPublic(java.awt.print.Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.isPublic = false ORDER BY m.createdDate DESC")
    List<MessageEntity> findLatestMessagesNotPublic(Pageable pageable);

}
