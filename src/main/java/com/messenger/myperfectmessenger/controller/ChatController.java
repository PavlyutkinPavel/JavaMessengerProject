package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController //для REST архитектуры
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final SecurityService securityService;

    private final SecurityCredentialsRepository securityCredentialsRepository;

    public ChatController(ChatService chatService, SecurityService securityService, SecurityCredentialsRepository securityCredentialsRepository) {
        this.chatService = chatService;
        this.securityService = securityService;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getChats(Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            List<Chat> chats = chatService.getChats();
            if (chats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(chats, HttpStatus.OK);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/chat_list_of_user/{username}")
    public ResponseEntity<List<Chat>> getChatList(Principal principal, @PathVariable String username) {
        if(username == principal.getName() || securityService.checkIfAdmin(principal.getName())){
            List<Chat> chats = chatService.getChatList(securityService.getUserIdByLogin(principal.getName()));
            if (chats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(chats, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //no security because chat don't have active user
    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChat(@PathVariable Long id, Principal principal) {
        Chat chat = chatService.getChat(id, principal);
        if(chat != null){
            return new ResponseEntity<>(chat, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long id, Principal principal) {
        List<Message> messages = chatService.getChatMessages(id, principal);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    // Найти определенное сообщение в чате по его id
    @GetMapping("/{chatId}/find_message/{messageId}")
    public ResponseEntity<Message> findMessage(@PathVariable Long chatId, @PathVariable Long messageId, Principal principal) {
        Message message = chatService.findMessage(chatId, messageId, principal);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createChat(@RequestBody Chat chat) {
        chatService.createChat(chat);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateChat(@RequestBody Chat chat, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.updateChat(chat);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update_name/{id}")
    public ResponseEntity<HttpStatus> updateChatName(@PathVariable Long id, @RequestParam String newChatName, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.updateChatName(id, newChatName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update_description/{id}")
    public ResponseEntity<HttpStatus> updateChatDescription(@PathVariable Long id, @RequestParam String newChatDescription, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.updateChatDescription(id, newChatDescription);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable Long id, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.deleteChatById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Очистить все сообщения в чате по его id
    @DeleteMapping("/clear_messages/{chatId}")
    public ResponseEntity<HttpStatus> clearAllMessages(@PathVariable Long chatId, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.clearAllMessages(chatId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Очистить определенное сообщение в чате по его id
    @DeleteMapping("/{chatId}/delete_message/{messageId}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable Long chatId, @PathVariable Long messageId, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.deleteMessage(chatId, messageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{chatId}/delete_user/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long chatId, @PathVariable Long userId, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            chatService.deleteUser(userId, chatId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{chatId}/leave_chat/{username}")
    public ResponseEntity<HttpStatus> leaveChat(@PathVariable Long chatId, @PathVariable String username, Principal principal) {
        deleteUser(chatId, securityService.getUserIdByLogin(username),principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}