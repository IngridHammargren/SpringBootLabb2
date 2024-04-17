package se.iths.springbootlabb2.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.repositories.MessageRepository;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public Optional<MessageEntity> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public Iterable<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }
    public void saveMessage(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }
}
