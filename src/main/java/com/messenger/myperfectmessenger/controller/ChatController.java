package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.Message;
import com.messenger.myperfectmessenger.exception.ChatNotFoundException;
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

import java.util.List;

@RestController //для REST архитектуры
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getChats() {
        List<Chat> chats = chatService.getChats();
        if (chats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(chats, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChat(@PathVariable Long id) {
        Chat chat = chatService.getChat(id).orElseThrow(ChatNotFoundException::new);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long chatId) {
        List<Message> messages = chatService.getChatMessages(chatId);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    // Найти определенное сообщение в чате по его id
    @GetMapping("/{chatId}/find-message/{messageId}")
    public ResponseEntity<Message> findMessage(@PathVariable Long chatId, @PathVariable Long messageId) {
        Message message = chatService.findMessage(chatId, messageId);
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
    public ResponseEntity<HttpStatus> updateChat(@RequestBody Chat chat) {
        chatService.updateChat(chat);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update_name/{id}")
    public ResponseEntity<HttpStatus> updateChatName(@PathVariable Long id, @RequestParam String newChatName) {
        chatService.updateChatName(id, newChatName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update_description/{id}")
    public ResponseEntity<HttpStatus> updateChatDescription(@PathVariable Long id, @RequestParam String newChatDescription) {
        chatService.updateChatDescription(id, newChatDescription);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable Long id) {
        chatService.deleteChatById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Очистить все сообщения в чате по его id
    @DeleteMapping("/clear-messages/{chatId}")
    public ResponseEntity<HttpStatus> clearAllMessages(@PathVariable Long chatId) {
        chatService.clearAllMessages(chatId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Очистить определенное сообщение в чате по его id
    @DeleteMapping("/{chatId}/delete-message/{messageId}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable Long chatId, @PathVariable Long messageId) {
        chatService.deleteMessage(chatId, messageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //TODO: add deleting users from chat(for admins)
}