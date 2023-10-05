package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.ChatRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChats() {
        return chatRepository.findAll(Sort.by("id"));
    }

    public Optional<Chat> getChat(Long id) {
        //проверка на логин
        return chatRepository.findById(id);
    }
    public void createChat(Chat chat) {
        chatRepository.save(chat);
    }

    public void updateChat(Chat chat) {
        chatRepository.saveAndFlush(chat);
    }

    public void deleteChatById(Long id){
        chatRepository.deleteById(id);
    }

    public void clearAllMessages(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            chat.getMessages().clear();
            chatRepository.save(chat);
        }
    }

    public void deleteMessage(Long chatId, Long messageId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            chat.getMessages().removeIf(message -> message.getId().equals(messageId));
            chatRepository.save(chat);
        }
    }

    public Message findMessage(Long chatId, Long messageId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            return chat.getMessages().stream()
                    .filter(message -> message.getId().equals(messageId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public List<Message> getChatMessages(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            return chat.getMessages().stream().toList();
        }
        return null;
    }

    public void updateChatName(Long chatId, String newChatName) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(UserNotFoundException::new);
        chat.setChatName(newChatName);
        chatRepository.saveAndFlush(chat);
    }

    public void updateChatDescription(Long chatId, String newChatDescription) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(UserNotFoundException::new);
        chat.setDescription(newChatDescription);
        chatRepository.saveAndFlush(chat);
    }
}