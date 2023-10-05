package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.repository.MessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return messageRepository.findAll(Sort.by("id"));
    }

    public Optional<Message> getMessage(Long id) {
        //проверка на логин
        return messageRepository.findById(id);
    }
    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    public void updateMessage(Message message) {
        messageRepository.saveAndFlush(message);
    }

    public void deleteMessageById(Long id){
        messageRepository.deleteById(id);
    }

    public void deleteAllMessages(){
        messageRepository.deleteAll();
    }
}
