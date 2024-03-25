package se.iths.springbootlabb2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.repositories.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public Optional<MessageEntity> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public Iterable<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }
    public MessageEntity saveMessage(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }
}
