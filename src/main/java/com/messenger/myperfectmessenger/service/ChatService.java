package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.exception.ChatNotFoundException;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.ChatRepository;
import com.messenger.myperfectmessenger.repository.UserChatRelationRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserChatRelationRepository userChatRelationRepository;
    private final SecurityCredentialsRepository securityCredentialsRepository;

    public ChatService(ChatRepository chatRepository, UserChatRelationRepository userChatRelationRepository, SecurityCredentialsRepository securityCredentialsRepository) {
        this.chatRepository = chatRepository;
        this.userChatRelationRepository = userChatRelationRepository;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    public List<Chat> getChats() {
        return chatRepository.findAll(Sort.by("id"));
    }

    public List<Chat> getChatList(Long userId) {
        List<Object[]> results = userChatRelationRepository.findAllChatByUserId(userId);
        List<Chat> chats = new ArrayList<>();

        for (Object[] result : results) {
            Chat chat = new Chat();
            chat.setId((Long) result[0]);
            chat.setChatName((String) result[1]);
            chat.setDescription((String) result[2]);
            chat.setUsers((Collection<User>) result[3]);
            chat.setMessages((Collection<Message>) result[4]);
            chats.add(chat);
        }

        return chats;
    }

    public Chat getChat(Long id, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        Chat chat =  chatRepository.findById(id).orElseThrow(ChatNotFoundException::new);
        Role role = credentials.getUserRole();
        Boolean ifChatMember = false;
        List<Chat> chatList = getChatList(currentUserId);
        for (Chat myChat: chatList){
            if(myChat.getId() == chat.getId()){
                ifChatMember = true;
            }
        }
        if( ifChatMember || (role.toString() == "ADMIN")){
            return chat;
        }else{
            return null;
        }
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

    public void deleteUser(Long userId, Long chatId) {
        userChatRelationRepository.removeUserByIdAndChatId(userId, chatId);
    }

    public Message findMessage(Long chatId, Long messageId, Principal principal) {

        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        Chat chatResult =  chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        Role role = credentials.getUserRole();
        Boolean ifChatMember = false;
        List<Chat> chatList = getChatList(currentUserId);
        for (Chat myChat: chatList){
            if(myChat.getId() == chatResult.getId()){
                ifChatMember = true;
            }
        }
        if( ifChatMember || (role.toString() == "ADMIN")){
            if (chatResult != null) {
                if (chatResult != null) {
                    return chatResult.getMessages().stream()
                            .filter(message -> message.getId().equals(messageId))
                            .findFirst()
                            .orElse(null);
                }
            }
        }else{
            return null;
        }
        return null;
    }

    public List<Message> getChatMessages(Long chatId, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        Chat chatResult =  chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        Role role = credentials.getUserRole();
        Boolean ifChatMember = false;
        List<Chat> chatList = getChatList(currentUserId);
        for (Chat myChat: chatList){
            if(myChat.getId() == chatResult.getId()){
                ifChatMember = true;
            }
        }
        if( ifChatMember || (role.toString() == "ADMIN")){
            if (chatResult != null) {
                return chatResult.getMessages().stream().toList();
            }
        }else{
            return null;
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