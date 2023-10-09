package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.exception.MessageNotFoundException;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.MessageRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final SecurityCredentialsRepository securityCredentialsRepository;

    public MessageService(MessageRepository messageRepository, SecurityCredentialsRepository securityCredentialsRepository) {
        this.messageRepository = messageRepository;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    public List<Message> getMessages() {
        return messageRepository.findAll(Sort.by("id"));
    }

    public Message getMessage(Long id, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Message message =  messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        Role role = credentials.getUserRole();
        if((principal.getName() == message.getSender()) || (role.toString() == "ADMIN")){
            return message;
        }else{
            return null;
        }
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
